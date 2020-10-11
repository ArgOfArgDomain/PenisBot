package penisbot.command;

import penisbot.module.BaseModule;

import java.util.ArrayList;

public class CommandRepository {

    private ArrayList<CommandRepositoryEntry> commandRepositoryEntries = new ArrayList<>();

    public CommandRepository() {

    }

    public void add(CommandSpecification specification, CommandHandler handler, BaseModule module) {
        CommandRepositoryEntry entry = new CommandRepositoryEntry(specification, module);
        entry.addHandler(handler);
        commandRepositoryEntries.add(entry);
    }

    public CommandRepositoryEntry find(String command) {
        for (CommandRepositoryEntry commandRepositoryEntry : commandRepositoryEntries) {
            CommandSpecification commandSpecification = commandRepositoryEntry.getCommandSpecification();

            // Check name
            if (commandSpecification.getName().equalsIgnoreCase(command)) {
                return commandRepositoryEntry;
            }

            // Check aliases
            for (String alias : commandSpecification.getAliases()) {
                if (alias.equalsIgnoreCase(command)) {
                    return commandRepositoryEntry;
                }
            }
        }

        return null;
    }

}
