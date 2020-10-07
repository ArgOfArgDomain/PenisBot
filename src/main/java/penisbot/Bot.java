package penisbot;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.configuration.Configuration;

import java.io.IOException;

public class Bot extends PircBot {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Configuration configuration;

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
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        super.onPrivateMessage(sender, login, hostname, message);
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        super.onPart(channel, sender, login, hostname);
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        super.onNickChange(oldNick, login, hostname, newNick);
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
    }
}
