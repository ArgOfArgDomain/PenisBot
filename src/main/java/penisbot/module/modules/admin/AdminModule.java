package penisbot.module.modules.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.Bot;
import penisbot.command.argument.ArgumentSpecificationException;
import penisbot.module.BaseModule;
import penisbot.module.ModuleInitializationException;
import penisbot.module.annotation.Module;
import penisbot.module.modules.admin.commands.JoinCommand;

@Module
public class AdminModule extends BaseModule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AdminModule(Bot bot) throws ModuleInitializationException {
        super(bot);

        try {
            // Register the commands this module processes
            registerCommands();
        } catch (ArgumentSpecificationException e) {
            logger.error("Error initializing module", e);
            throw new ModuleInitializationException(e);
        }
    }

    private void registerCommands() throws ArgumentSpecificationException {
        JoinCommand joinCommand = new JoinCommand(this);

        // TODO: actually register
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
