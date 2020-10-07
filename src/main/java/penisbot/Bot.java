package penisbot;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.configuration.Configuration;
import penisbot.state.UserRepository;

import java.io.IOException;

public class Bot extends PircBot {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Configuration configuration;
    private final UserRepository userRepository = new UserRepository();

    public Bot(Configuration configuration) {
        super();

        this.configuration = configuration;

        // Extract configuration into underlying framework
        setName(configuration.getNickname());
        setLogin(configuration.getLoginName());

        // Some currently hard coded options
        setAutoNickChange(true);
        setVersion("PEN1SBOT");


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
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        super.onPrivateMessage(sender, login, hostname, message);
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
        if (nickname == getName()) {
            // Remove everyone from this channel, as we no longer know about what
            // is going on in there.
            userRepository.removeChannel(channel);
        } else {
            // Remove from channel in our user repository
            userRepository.removeUserFromChannel(nickname, channel);
        }
    }
}
