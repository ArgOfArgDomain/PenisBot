package penisbot.module.modules.admin.commands;

import penisbot.Bot;
import penisbot.command.Command;
import penisbot.command.CommandHandler;
import penisbot.command.CommandSpecification;
import penisbot.command.argument.ArgumentSpecificationException;
import penisbot.module.modules.admin.AdminModule;

public class JoinCommand extends AdminCommandSpecification implements CommandHandler {

    public JoinCommand(AdminModule adminModule) throws ArgumentSpecificationException {
        super("join");
    }

    @Override
    public void handle(Bot bot, Command command) {

    }
}
