package penisbot.command.argument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsSpecification {

    private final List<PositionalArgumentSpecification> positionalArgumentSpecifications = new ArrayList<>();
    private final List<FlagArgumentSpecification> flagArgumentSpecifications = new ArrayList<>();

    public void addPositionalArgument(PositionalArgumentSpecification positionalArgumentSpecification) throws ArgumentSpecificationException {
        positionalArgumentSpecifications.add(positionalArgumentSpecification);
    }

    public void addFlagArgument(FlagArgumentSpecification flagArgumentSpecification) throws ArgumentSpecificationException {
        flagArgumentSpecifications.add(flagArgumentSpecification);
    }

    public ParsedArguments parse(String userInput) throws ArgumentUserInputException {
        return ParsedArguments.parse(this, userInput);
    }

    public List<PositionalArgumentSpecification> getPositionalArgumentSpecifications() {
        return positionalArgumentSpecifications;
    }

    public List<FlagArgumentSpecification> getFlagArgumentSpecifications() {
        return flagArgumentSpecifications;
    }

    public FlagArgumentSpecification getFlagArgumentSpecificationByName(String name) {

        for (FlagArgumentSpecification flagArgumentSpecification : flagArgumentSpecifications) {
            if (flagArgumentSpecification.getName().equalsIgnoreCase(name)) {
                return flagArgumentSpecification;
            }
        }

        return null;
    }

    public FlagArgumentSpecification getFlagArgumentSpecificationByShortName(String shortName) {

        for (FlagArgumentSpecification flagArgumentSpecification : flagArgumentSpecifications) {
            if (flagArgumentSpecification.getShortName().equalsIgnoreCase(shortName)) {
                return flagArgumentSpecification;
            }
        }

        return null;
    }
}
