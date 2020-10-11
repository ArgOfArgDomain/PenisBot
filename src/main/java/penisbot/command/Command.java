package penisbot.command;

import penisbot.command.argument.ParsedArguments;
import penisbot.state.UserRepositoryEntry;

import java.util.List;

public class Command {

    private final CommandSpecification commandSpecification;
    private final String sender;
    private final String target;
    private final ParsedArguments parsedArguments;

    public Command(CommandSpecification commandSpecification, String sender, String target, ParsedArguments parsedArguments) {
        this.commandSpecification = commandSpecification;
        this.sender = sender;
        this.target = target;
        this.parsedArguments = parsedArguments;
    }

    public CommandSpecification getCommandSpecification() {
        return commandSpecification;
    }

    public String getTarget() {
        return target;
    }

    public ParsedArguments getParsedArguments() {
        return parsedArguments;
    }

    public String getSender() {
        return sender;
    }
}
