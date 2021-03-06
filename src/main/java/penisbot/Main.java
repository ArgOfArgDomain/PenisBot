/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package penisbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.configuration.Configuration;
import penisbot.configuration.ConfigurationException;
import penisbot.module.ModuleInitializationException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        try {
            // Load configuration
            Configuration configuration = new Configuration();
            configuration.populateFromEnv();

            // Create our bot
            Bot bot = new Bot(configuration);

            // Start it up
            bot.start();

        } catch (ConfigurationException | ModuleInitializationException e) {
            logger.error("Fatal error occurred.", e);
            System.exit(1);
        }
    }
}
