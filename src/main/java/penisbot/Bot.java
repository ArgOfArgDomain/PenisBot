package penisbot;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.command.*;
import penisbot.command.argument.ArgumentSpecification;
import penisbot.command.argument.ArgumentUserInputException;
import penisbot.command.argument.ArgumentsSpecification;
import penisbot.command.argument.ParsedArguments;
import penisbot.configuration.Configuration;
import penisbot.module.BaseModule;
import penisbot.module.ModuleInitializationException;
import penisbot.module.ModuleRepository;
import penisbot.state.UserRepository;
import penisbot.state.UserRepositoryEntry;

import java.io.IOException;
import java.util.*;

public class Bot extends PircBot {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Configuration configuration;
    private final UserRepository userRepository = new UserRepository();
    private final CommandRepository commandRepository = new CommandRepository();
    private final ModuleRepository moduleRepository;



    public Bot(Configuration configuration) throws ModuleInitializationException {
        super();

        this.configuration = configuration;

        // Extract configuration into underlying framework
        setName(configuration.getNickname());
        setLogin(configuration.getLoginName());

        // Some currently hard coded options
        setAutoNickChange(true);
        setVersion("PEN1SBOT");

        // Find and initialize modules
        moduleRepository = new ModuleRepository(this);
        moduleRepository.loadModules();
    }



    public void start() {

        // One we have connected successfully once, re-connect will be handled via
        // disconnect handler. We thus just need to ensure we successfully connect at
        // least once.
        while (!isConnected()) {
            try {
                // Connect
                logger.info("Attempting connection");
                connect(configuration.getServerHostname(), configuration.getServerPort(), configuration.getServerPassword());
            } catch (IOException | IrcException e) {
                logger.error("Error connecting", e);

                // Wait a bit
                Utilities.trySleep(5000);
            }
        }

    }

    @Override
    protected void onDisconnect() {
        super.onDisconnect();

        logger.info("Disconnected");

        // Clear entire user repository
        userRepository.clearAll();

        // Attempt to reconnect forever
        // We are at this point blocking the main bot thread, but this should not matter.
        while (!isConnected()) {
            try {
                logger.info("Attempting reconnection");
                reconnect();
            }
            catch (IOException | IrcException e) {
                logger.error("Error connecting", e);

                // Wait a bit
                Utilities.trySleep(5000);
            }
        }
    }

    @Override
    protected void onConnect() {
        super.onConnect();

        logger.info("Connected");

    }

    @Override
    protected void onUserList(String channel, User[] users) {
        super.onUserList(channel, users);

        // Add to our user repository
        for (User user : users) {
            userRepository.upsertUser(user.getNick(), channel);
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);

        // Add to our user repository
        userRepository.upsertUser(sender, channel);
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        super.onPart(channel, sender, login, hostname);
        logger.debug("User " + sender + " left channel " + channel);

        // Generic "user (possibly us) left for reason" handling
        handleUserLeavingChannel(channel, sender);

    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        super.onNickChange(oldNick, login, hostname, newNick);
        logger.debug("User " + oldNick + " changed name to " + newNick);

        // Rename in our user repository
        userRepository.renameUser(oldNick, newNick);
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
        logger.debug("User " + recipientNick + " kicked from channel " + channel);

        // Generic "user (possibly us) left for reason" handling
        handleUserLeavingChannel(channel, recipientNick);
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
        logger.debug("User " + sourceNick + " quit");

        // Remove from our user repository entirely
        userRepository.removeUser(sourceNick);

    }

    private void handleUserLeavingChannel(String channel, String nickname) {
        // Was this us?
        if (nickname.equalsIgnoreCase(getName())) {
            // Remove everyone from this channel, as we no longer know about what
            // is going on in there.
            userRepository.removeChannel(channel);
        } else {
            // Remove from channel in our user repository
            userRepository.removeUserFromChannel(nickname, channel);
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        super.onPrivateMessage(sender, login, hostname, message);
        logger.debug("Private message from " + sender + ": " + message);

        processMessage(message, sender, sender);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        super.onMessage(channel, sender, login, hostname, message);
        logger.debug("Message from " + sender + " in " + channel + ": " + message);

        processMessage(message, sender, channel);
    }

    private void processMessage(String message, String sender, String target) {

        // Was this a private message?
        boolean privateMessage = target.equalsIgnoreCase(sender);

        // Split off first token
        String tokens[] = message.split("\\s+", 2);
        String token = tokens[0];
        String remainder = tokens.length == 2 ? tokens[1] : "";

        // Starts with command prefix?
        if (token.startsWith(configuration.getCommandPrefix())) {
            String command = stripCommandPrefix(token);
            processCommand(command, remainder, sender, target);
        } else {
            // Addressed to us?
            // We remove any potential suffix (: or ,) first, then check
            if (token.replaceAll("[,:]$", "").equalsIgnoreCase(getNick())) {
                // Split again
                tokens = remainder.split("\\s+", 2);
                token = tokens[0];
                remainder = tokens.length == 2 ? tokens[1] : "";

                // Extract command and remainder
                String command = stripCommandPrefix(token);

                // Process
                processCommand(command, remainder, sender, target);
            } else {
                // Otherwise, if it was sent to us as a private message, we assume it was a command
                if (privateMessage) {
                    // Extract command and remainder
                    String command = stripCommandPrefix(token);

                    // Process
                    processCommand(command, remainder, sender, target);
                }
            }
        }
    }

    private String stripCommandPrefix(String command) {
        if (command.startsWith(configuration.getCommandPrefix())) {
            command = command.substring(configuration.getCommandPrefix().length());
        }
        return command;
    }

    private void processCommand(String command, String remainder, String sender, String target) {
        // TODO: filter sensitive somehow, or just not show remainder maybe?
        logger.debug("Processing potential command: command=" + command + ", remainder=" + remainder + ", sender=" + sender + ", target=" + target);

        // Do we have this command in our repository?
        CommandRepositoryEntry commandRepositoryEntry = commandRepository.find(command);
        if (commandRepositoryEntry != null) {
            logger.debug("Found entry in our command repository");

            // Grab our spec
            CommandSpecification commandSpecification = commandRepositoryEntry.getCommandSpecification();

            try {
                // Deal with arguments
                ArgumentsSpecification argumentSpecification = commandSpecification.getArgumentsSpecification();
                ParsedArguments parsedArguments = argumentSpecification.parse(remainder);

                // Build our final command object
                Command commandObj = new Command(commandSpecification, sender, target, parsedArguments);

                // Handle it
                logger.debug("Calling handlers");
                for (CommandHandler handler : commandRepositoryEntry.getHandlers()) {
                    logger.debug("Calling handler: " + handler.getClass().getName());
                    handler.handle(commandObj);
                }
            } catch (ArgumentUserInputException e) {
                logger.debug("User argument error: " + e.getMessage());
                // TODO: inform the users!
            }
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public CommandRepository getCommandRepository() {
        return commandRepository;
    }

    public ModuleRepository getModuleRepository() {
        return moduleRepository;
    }

    public void registerCommand(CommandSpecification specification, CommandHandler handler, BaseModule module) {
        commandRepository.add(specification, handler, module);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
