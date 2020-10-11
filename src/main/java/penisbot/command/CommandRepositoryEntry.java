package penisbot.command;

import penisbot.module.BaseModule;

import java.util.ArrayList;

public class CommandRepositoryEntry {

    private final CommandSpecification commandSpecification;
    private final BaseModule module;
    private ArrayList<CommandHandler> handlers = new ArrayList<>();

    public CommandRepositoryEntry(CommandSpecification commandSpecification, BaseModule module) {
        this.commandSpecification = commandSpecification;
        this.module = module;
    }

    public void addHandler(CommandHandler handler) {
        handlers.add(handler);
    }

    public CommandSpecification getCommandSpecification() {
        return commandSpecification;
    }

    public BaseModule getModule() {
        return module;
    }

    public ArrayList<CommandHandler> getHandlers() {
        return handlers;
    }
}
