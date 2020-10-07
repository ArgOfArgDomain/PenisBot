package penisbot.state;

import java.util.ArrayList;

public class UserRepository {

    // Our collection of users
    private ArrayList<User> users = new ArrayList<>();

    public UserRepository() {

    }

    public synchronized void clearAll() {
        users.clear();
    }

    public synchronized void clearAllInChannel(String channelName) {
        users.removeIf(user -> user.isInChannel(channelName));
    }

}
