package penisbot.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class UserRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Our collection of users
    // We could have various maps to nickname and channel to make lookups faster, but given
    // the small size, just iterating through each time is probably not a big deal.
    private final ArrayList<UserRepositoryEntry> userRepositoryEntries = new ArrayList<>();

    public UserRepository() {

    }

    public synchronized void clearAll() {
        logger.debug("Clearing all users from repository");
        userRepositoryEntries.clear();
    }

    public synchronized void removeChannel(String channelName) {
        logger.debug("Removing " + channelName + " from all users in repository");

        // Remove channel
        for (UserRepositoryEntry userRepositoryEntry : userRepositoryEntries) {
            userRepositoryEntry.removeChannel(channelName);
        }

        // Prune any users no longer in any channels
        userRepositoryEntries.removeIf(userRepositoryEntry -> userRepositoryEntry.getChannels().size() == 0);
    }

    public synchronized void removeUserFromChannel(String nickname, String channelName) {
        logger.debug("Removing " + channelName + " from user " + nickname + " in repository");

        // Grab user
        UserRepositoryEntry userRepositoryEntry = getUser(nickname);
        if (userRepositoryEntry != null) {
            // Remove channel from user
            userRepositoryEntry.removeChannel(channelName);

            // Remove user if no longer in any channels we know about
            if (userRepositoryEntry.getChannels().size() == 0) {
                userRepositoryEntries.remove(userRepositoryEntry);
            }
        }
    }

    public synchronized void removeUser(String nickname) {
        logger.debug("Removing user " + nickname + " in repository");

        userRepositoryEntries.removeIf(userRepositoryEntry -> userRepositoryEntry.getNickname().equalsIgnoreCase(nickname));
    }

    public synchronized UserRepositoryEntry getUser(String nickname) {
        for (UserRepositoryEntry userRepositoryEntry : userRepositoryEntries) {
            if (userRepositoryEntry.getNickname().equalsIgnoreCase(nickname)) {
                return userRepositoryEntry;
            }
        }
        return null;
    }

    public synchronized void upsertUser(String nickname, String channel) {
        logger.debug("Upserting user " + nickname + " in channel " + channel + " in repository");

        // Add user if doesn't exist
        UserRepositoryEntry userRepositoryEntry = getUser(nickname);
        if (userRepositoryEntry == null) {
            userRepositoryEntry = new UserRepositoryEntry(nickname);
            userRepositoryEntries.add(userRepositoryEntry);
        }

        // Add channel
        userRepositoryEntry.addChannel(channel);
    }

    public synchronized void renameUser(String oldNickname, String newNickname) {
        logger.debug("Renaming user " + oldNickname + " to " + newNickname + " in repository");

        // Grab our user
        UserRepositoryEntry userRepositoryEntry = getUser(oldNickname);
        if (userRepositoryEntry != null) {
            userRepositoryEntry.setNickname(newNickname);
        }
    }

    public synchronized boolean hasUser(String nickname) {
        return getUser(nickname) != null;
    }
}
