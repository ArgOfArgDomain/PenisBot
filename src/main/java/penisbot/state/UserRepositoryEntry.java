package penisbot.state;

import java.util.ArrayList;
import java.util.Collection;

public class UserRepositoryEntry {

    private String nickname;
    private final ArrayList<String> channels = new ArrayList<>();
    private boolean admin;

    public UserRepositoryEntry(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Collection<String> getChannels() {
        return channels;
    }

    public void addChannel(String channelName) {
        if (!isInChannel(channelName)) {
            channels.add(channelName);
        }
    }

    public void removeChannel(String channelName) {
        if (isInChannel(channelName)) {
            channels.remove(channelName);
        }
    }

    public boolean isInChannel(String channelName) {
        return channels.contains(channelName);
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
