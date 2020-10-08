package penisbot.configuration;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;


public class ConfigurationTest {

    @Test
    public void configurationLoadedFromEnvironment() throws ConfigurationException {
        // Our configuration
        Configuration configuration = new Configuration();

        // Create our dummy environment
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PENISBOT_SERVER_HOSTNAME", "abcd");
        environment.put("PENISBOT_SERVER_PORT", "1234");

        // Load it
        configuration.populateFromEnv(environment);

        // Validate
        assertEquals("abcd", configuration.getServerHostname());
        assertEquals(1234, configuration.getServerPort());
    }

}
