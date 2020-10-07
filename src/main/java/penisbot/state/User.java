package penisbot.state;

import java.util.ArrayList;
import java.util.Collection;

public class User {

    private String nickname;
    private ArrayList<String> channels;
    private boolean admin;

    public User(String nickname) {
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
