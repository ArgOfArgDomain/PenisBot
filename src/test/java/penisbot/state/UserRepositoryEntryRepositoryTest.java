package penisbot.state;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserRepositoryEntryRepositoryTest {

    @Test
    public void userAddedToRepository() {
        // Our user repository
        UserRepository userRepository = new UserRepository();

        // User joins
        userRepository.upsertUser("testuser", "testchannel");
        assertEquals("testuser", userRepository.getUser("testuser").getNickname());
        assertTrue (userRepository.getUser("testuser").getChannels().contains("testchannel"));

    }

    @Test
    public void userRemovedFromRepositoryOnQuit() {
        // Our user repository
        UserRepository userRepository = new UserRepository();

        // User joins
        userRepository.upsertUser("testuser", "testchannel");
        assertEquals("testuser", userRepository.getUser("testuser").getNickname());
        assertTrue (userRepository.getUser("testuser").getChannels().contains("testchannel"));

        // User quits
        userRepository.removeUser("testuser");
        assertFalse(userRepository.hasUser("testuser"));
    }

    @Test
    public void userRemovedWhenLeavesChannel() {
        // Our user repository
        UserRepository userRepository = new UserRepository();

        // User joins
        userRepository.upsertUser("testuser", "testchannel");
        assertEquals("testuser", userRepository.getUser("testuser").getNickname());
        assertTrue (userRepository.getUser("testuser").getChannels().contains("testchannel"));

        // User parts
        userRepository.removeUserFromChannel("testuser", "testchannel");

        // User should no longer exist
        assertFalse(userRepository.hasUser("testuser"));
    }

    @Test
    public void multipleUsersMultipleChannelsUserRemovedWhenLeavesChannel() {
        // Our user repository
        UserRepository userRepository = new UserRepository();

        // Users join channels
        userRepository.upsertUser("testuser1", "testchannel1");
        userRepository.upsertUser("testuser2", "testchannel1");
        userRepository.upsertUser("testuser1", "testchannel2");
        userRepository.upsertUser("testuser2", "testchannel2");

        // Validate
        assertTrue(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser1", userRepository.getUser("testuser1").getNickname());
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel2"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // user 1 leaves channel 1
        userRepository.removeUserFromChannel("testuser1", "testchannel1");

        // Validate
        assertTrue(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser1", userRepository.getUser("testuser1").getNickname());
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertFalse (userRepository.getUser("testuser1").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel2"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // user 2 leaves channel 2
        userRepository.removeUserFromChannel("testuser2", "testchannel2");

        // Validate
        assertTrue(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser1", userRepository.getUser("testuser1").getNickname());
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertFalse (userRepository.getUser("testuser1").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel2"));
        assertFalse (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // user 1 leaves channel 2
        userRepository.removeUserFromChannel("testuser1", "testchannel2");

        // Validate
        assertFalse(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertFalse (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // user 2 leaves channel 1
        userRepository.removeUserFromChannel("testuser2", "testchannel1");

        // Validate
        assertFalse(userRepository.hasUser("testuser1"));
        assertFalse(userRepository.hasUser("testuser2"));
    }

    @Test
    public void multipleUsersMultipleChannelsUserRemovedWhenBotLeavesChannel() {
        // Our user repository
        UserRepository userRepository = new UserRepository();

        // Users join channels
        userRepository.upsertUser("testuser1", "testchannel1");
        userRepository.upsertUser("testuser2", "testchannel1");
        userRepository.upsertUser("testuser1", "testchannel2");
        userRepository.upsertUser("testuser2", "testchannel2");

        // Validate
        assertTrue(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser1", userRepository.getUser("testuser1").getNickname());
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel2"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // user 1 leaves channel 1
        userRepository.removeUserFromChannel("testuser1", "testchannel1");

        // Validate
        assertTrue(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser1", userRepository.getUser("testuser1").getNickname());
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertFalse (userRepository.getUser("testuser1").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel2"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // Bot leaves channel 2
        userRepository.removeChannel ("testchannel2");

        // Validate
        assertFalse(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertFalse (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // Bot leaves channel 1
        userRepository.removeChannel ("testchannel1");

        // Validate
        assertFalse(userRepository.hasUser("testuser1"));
        assertFalse(userRepository.hasUser("testuser2"));
    }

    @Test
    public void userRenamed() {
        // Our user repository
        UserRepository userRepository = new UserRepository();

        // User joins
        userRepository.upsertUser("testuser", "testchannel");
        assertEquals("testuser", userRepository.getUser("testuser").getNickname());
        assertTrue (userRepository.getUser("testuser").getChannels().contains("testchannel"));

        // Get our user under current nick
        UserRepositoryEntry userRepositoryEntry = userRepository.getUser("testuser");

        // User changes nick
        userRepository.renameUser("testuser", "testuser2");

        // Validate
        assertEquals("testuser2", userRepositoryEntry.getNickname());
        assertTrue(userRepository.hasUser("testuser2"));
        assertFalse(userRepository.hasUser("testuser"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel"));

    }

    @Test
    public void usersRemovedWhenBotQuits() {
        // Our user repository
        UserRepository userRepository = new UserRepository();

        // Users join channels
        userRepository.upsertUser("testuser1", "testchannel1");
        userRepository.upsertUser("testuser2", "testchannel1");
        userRepository.upsertUser("testuser1", "testchannel2");
        userRepository.upsertUser("testuser2", "testchannel2");

        // Validate
        assertTrue(userRepository.hasUser("testuser1"));
        assertTrue(userRepository.hasUser("testuser2"));
        assertEquals("testuser1", userRepository.getUser("testuser1").getNickname());
        assertEquals("testuser2", userRepository.getUser("testuser2").getNickname());
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel1"));
        assertTrue (userRepository.getUser("testuser1").getChannels().contains("testchannel2"));
        assertTrue (userRepository.getUser("testuser2").getChannels().contains("testchannel2"));

        // Bot quits
        userRepository.clearAll();

        // Validate
        assertFalse(userRepository.hasUser("testuser1"));
        assertFalse(userRepository.hasUser("testuser2"));
    }

}

