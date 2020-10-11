package penisbot.module.modules.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.Bot;
import penisbot.command.Command;
import penisbot.command.argument.ArgumentSpecificationException;
import penisbot.module.BaseModule;
import penisbot.module.ModuleInitializationException;
import penisbot.module.annotation.Module;
import penisbot.module.modules.admin.commands.JoinCommand;
import penisbot.state.UserRepositoryEntry;

@Module
public class AdminModule extends BaseModule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AdminModule(Bot bot) throws ModuleInitializationException {
        super("admin", bot);

        try {
            // Register the commands this module processes
            registerCommands();
        } catch (ArgumentSpecificationException e) {
            logger.error("Error initializing module", e);
            throw new ModuleInitializationException(e);
        }
    }

    private void registerCommands() throws ArgumentSpecificationException, ModuleInitializationException {
        JoinCommand joinCommand = new JoinCommand(this);
        registerCommand(joinCommand, this);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public boolean checkPermissions(Command command) {
        logger.debug("Checking permissions for user " + command.getSender() + " to execute command " + command.getCommandSpecification().getName());

        // Is the user logged in as an admin?
        UserRepositoryEntry userRepositoryEntry = getBot().getUserRepository().getUser(command.getSender());
        if (userRepositoryEntry != null) {
            if (userRepositoryEntry.isAdmin()) {
                logger.debug("User is logged in as an admin");
                return true;
            } else {
                logger.debug("User is not logged in as an admin");
            }
        } else {
            logger.debug("User is not in user repository");
        }

        // Was the admin password specified
        String adminPassword = command.getParsedArguments().getValueFor("admin-password");
        if (adminPassword != null) {
            // Check
            String configuredAdminPassword = getBot().getConfiguration().getAdminPassword();
            if (adminPassword.equalsIgnoreCase(configuredAdminPassword)) {
                logger.debug("Admin password valid");
                return true;
            } else {
                logger.debug("Admin password specified via argument but does not match configuration");
            }
        } else {
            logger.debug("No admin password specified via argument");
        }

        logger.debug("User does not have permission to run command");
        return false;
    }
}
