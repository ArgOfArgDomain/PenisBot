package penisbot.module.modules.admin.commands;

import penisbot.Bot;
import penisbot.command.CommandSpecification;
import penisbot.command.argument.ArgumentSpecificationException;
import penisbot.command.argument.FlagArgumentSpecification;

public class AdminCommandSpecification extends CommandSpecification {

    public AdminCommandSpecification(String name) throws ArgumentSpecificationException {
        super(name);

        setAdminOnly(true);

        // Add flag to allow specification of password
        FlagArgumentSpecification adminPasswordFlag = new FlagArgumentSpecification(
                "admin-password", "p",
                "Admin password, required if not already identified in as an admin (via admin command)",
                false);
        addFlagArgument(adminPasswordFlag);
    }

}
