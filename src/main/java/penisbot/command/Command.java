package penisbot.command;

import penisbot.state.User;

import java.util.List;

public class Command {

    private final CommandSpecification commandSpecification;
    private final User user;
    private final String target;
    private final List<String> arguments;

    public Command(CommandSpecification commandSpecification, User user, String target, List<String> arguments) {
        this.commandSpecification = commandSpecification;
        this.user = user;
        this.target = target;
        this.arguments = arguments;
    }

    public CommandSpecification getCommandSpecification() {
        return commandSpecification;
    }

    public User getUser() {
        return user;
    }

    public String getTarget() {
        return target;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
