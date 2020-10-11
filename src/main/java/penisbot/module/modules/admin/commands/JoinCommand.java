package penisbot.module.modules.admin.commands;

import penisbot.Bot;
import penisbot.command.Command;
import penisbot.command.CommandHandler;
import penisbot.command.CommandSpecification;
import penisbot.command.argument.ArgumentSpecificationException;
import penisbot.command.argument.PositionalArgumentMultiplicity;
import penisbot.command.argument.PositionalArgumentSpecification;
import penisbot.module.modules.admin.AdminModule;

public class JoinCommand extends AdminCommandSpecification implements CommandHandler {

    private final AdminModule adminModule;

    public JoinCommand(AdminModule adminModule) throws ArgumentSpecificationException {
        super("join");

        this.adminModule = adminModule;

        // Take channel names as arguments
        addPositionalArgument("channel", "Join given channel(s)", PositionalArgumentMultiplicity.ONE_OR_MANY);
    }

    @Override
    public void handle(Command command) {
        // Verify user is either admin, or has specified password in command
        if (!adminModule.checkPermissions(command)) {
            return;
        }


        // Go through given channel names and join them
        for (String channelName : command.getParsedArguments().getValueFor("channel").split("\\s+")) {
            adminModule.getBot().joinChannel(channelName);
        }
    }
}
