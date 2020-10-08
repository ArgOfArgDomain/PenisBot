package penisbot.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class UserRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Our collection of users
    // We could have various maps to nickname and channel to make lookups faster, but given
    // the small size, just iterating through each time is probably not a big deal.
    private final ArrayList<User> users = new ArrayList<>();

    public UserRepository() {

    }

    public synchronized void clearAll() {
        logger.debug("Clearing all users from repository");
        users.clear();
    }

    public synchronized void removeChannel(String channelName) {
        logger.debug("Removing " + channelName + " from all users in repository");

        // Remove channel
        for (User user : users) {
            user.removeChannel(channelName);
        }

        // Prune any users no longer in any channels
        users.removeIf(user -> user.getChannels().size() == 0);
    }

    public synchronized void removeUserFromChannel(String nickname, String channelName) {
        logger.debug("Removing " + channelName + " from user " + nickname + " in repository");

        // Grab user
        User user = getUser(nickname);
        if (user != null) {
            // Remove channel from user
            user.removeChannel(channelName);

            // Remove user if no longer in any channels we know about
            if (user.getChannels().size() == 0) {
                users.remove(user);
            }
        }
    }

    public synchronized void removeUser(String nickname) {
        logger.debug("Removing user " + nickname + " in repository");

        users.removeIf(user -> user.getNickname().equalsIgnoreCase(nickname));
    }

    public synchronized User getUser(String nickname) {
        for (User user : users) {
            if (user.getNickname().equalsIgnoreCase(nickname)) {
                return user;
            }
        }
        return null;
    }

    public synchronized void upsertUser(String nickname, String channel) {
        logger.debug("Upserting user " + nickname + " in channel " + channel + " in repository");

        // Add user if doesn't exist
        User user = getUser(nickname);
        if (user == null) {
            user = new User(nickname);
            users.add(user);
        }

        // Add channel
        user.addChannel(channel);
    }

    public synchronized void renameUser(String oldNickname, String newNickname) {
        logger.debug("Renaming user " + oldNickname + " to " + newNickname + " in repository");

        // Grab our user
        User user = getUser(oldNickname);
        if (user != null) {
            user.setNickname(newNickname);
        }
    }

    public synchronized boolean hasUser(String nickname) {
        return getUser(nickname) != null;
    }
}
