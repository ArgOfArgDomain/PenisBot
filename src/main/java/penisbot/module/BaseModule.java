package penisbot.module;

import org.slf4j.Logger;
import penisbot.Bot;
import penisbot.command.Command;
import penisbot.command.CommandHandler;
import penisbot.command.CommandSpecification;

public abstract class BaseModule {

    private final String moduleName;
    private final Bot bot;

    public BaseModule(String moduleName, Bot bot) {
        this.moduleName = moduleName;
        this.bot = bot;
    }

    protected abstract Logger getLogger();

    public String getModuleName() {
        return moduleName;
    }

    public Bot getBot() {
        return bot;
    }

    public void registerCommand(CommandSpecification specification, BaseModule module) throws ModuleInitializationException {
        if (!CommandHandler.class.isAssignableFrom(specification.getClass())) {
            throw new ModuleInitializationException("Attempt to register command specification " + specification.getClass().getName() + " which does not implement a command handler.");
        }
        registerCommand(specification, (CommandHandler) specification, module);
    }

    public void registerCommand(CommandSpecification specification, CommandHandler handler, BaseModule module) {
        bot.registerCommand(specification, handler, module);
    }

}
