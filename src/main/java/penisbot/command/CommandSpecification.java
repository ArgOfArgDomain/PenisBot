package penisbot.command;

import penisbot.command.argument.ArgumentSpecificationException;
import penisbot.command.argument.ArgumentsSpecification;
import penisbot.command.argument.FlagArgumentSpecification;
import penisbot.command.argument.PositionalArgumentSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSpecification {

    private final String name;
    private final Collection<String> aliases = new ArrayList<>();

    private String summaryLine = "No summary available";
    private final List<String> helpLines = new ArrayList<>();
    private boolean privateOnly = false;
    private boolean adminOnly = false;

    private final ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();

    public CommandSpecification(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAlias(String name) {
        aliases.add(name);
    }

    public void addHelpLine(String line) {
        helpLines.add(line);
    }

    public void addHelpLines(Collection<String> lines) {
        helpLines.addAll(lines);
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    public String getSummaryLine() {
        return summaryLine;
    }

    public void setSummaryLine(String summaryLine) {
        this.summaryLine = summaryLine;
    }

    public List<String> getHelpLines() {
        return helpLines;
    }

    public boolean isPrivateOnly() {
        return privateOnly;
    }

    public void setPrivateOnly(boolean privateOnly) {
        this.privateOnly = privateOnly;
    }

    public boolean isAdminOnly() {
        return adminOnly;
    }

    public void setAdminOnly(boolean adminOnly) {
        this.adminOnly = adminOnly;
    }

    public void addPositionalArgument(PositionalArgumentSpecification positionalArgumentSpecification) throws ArgumentSpecificationException {
        argumentsSpecification.addPositionalArgument(positionalArgumentSpecification);
    }

    public void addFlagArgument(FlagArgumentSpecification flagArgumentSpecification) throws ArgumentSpecificationException {
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);
    }
}
