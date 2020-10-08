package penisbot.command.argument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class ParsedArguments {

    private final ArgumentsSpecification argumentsSpecification;
    private final HashMap<ArgumentSpecification, String> argumentValueMap;

    private ParsedArguments(ArgumentsSpecification argumentsSpecification, HashMap<ArgumentSpecification, String> argumentValueMap) {
        this.argumentsSpecification = argumentsSpecification;
        this.argumentValueMap = argumentValueMap;
    }

    public static ParsedArguments parse(ArgumentsSpecification argumentsSpecification, String userInput) throws ArgumentUserInputException {

        // Map to store values for our arguments, initialize with nulls for convenience
        HashMap<ArgumentSpecification, String> argumentValueMap = new HashMap<>();
        for (ArgumentSpecification argumentSpecification : argumentsSpecification.getPositionalArgumentSpecifications()) {
            argumentValueMap.put(argumentSpecification, null);
        }
        for (ArgumentSpecification argumentSpecification : argumentsSpecification.getFlagArgumentSpecifications()) {
            argumentValueMap.put(argumentSpecification, null);
        }

        // We will iterate through our positional arguments as we encounter things that are not flags
        // We store the one we are currently on, as we will need this if expecting multiplicity.
        Iterator<PositionalArgumentSpecification> positionalArgumentI = argumentsSpecification.getPositionalArgumentSpecifications().iterator();
        PositionalArgumentSpecification currentPositionalArgument = null;

        // Tokenize input and iterate through it
        Iterable<String> tokens = Arrays.asList(userInput.split("\\s+"));
        Iterator<String> tokenI = tokens.iterator();
        while (tokenI.hasNext()) {
            // Next token
            String token = tokenI.next();

            // Flag?
            if (token.startsWith("-")) {
                // See if we can find our flag
                FlagArgumentSpecification flagArgumentSpecification = null;
                if (token.startsWith("--")) {
                    String name = token.substring(2);
                    name = name.split("=", 2)[0];
                    flagArgumentSpecification = argumentsSpecification.getFlagArgumentSpecificationByName(name);
                } else {
                    String name = token.substring(1);
                    name = name.split("=", 2)[0];
                    flagArgumentSpecification = argumentsSpecification.getFlagArgumentSpecificationByShortName(name);
                }

                // Bail if we didn't
                if (flagArgumentSpecification == null) {
                    throw new ArgumentUserInputException("Invalid option specified: " + token);
                }

                // If this is a boolean flag, we set it to true, otherwise we need
                // to parse out a parameter
                if (flagArgumentSpecification.isBoolean()) {
                    argumentValueMap.put(flagArgumentSpecification, String.valueOf(true));
                } else {
                    // Was this in key=value form?
                    String[] keyValueTokens = token.split("=", 2);
                    if (keyValueTokens.length == 2) {
                        // Use it!
                        String value = keyValueTokens[1];
                        argumentValueMap.put(flagArgumentSpecification, value);
                    } else {
                        // Check for next parameter and use that
                        if (tokenI.hasNext()) {
                            String value = tokenI.next();

                            // Make sure not a flag
                            if (value.startsWith("-")) {
                                throw new ArgumentUserInputException("Parameter required for flag: " + token);
                            }

                            // Use it
                            argumentValueMap.put(flagArgumentSpecification, value);
                        } else {
                            throw new ArgumentUserInputException("Parameter required for flag: " + token);
                        }
                    }
                }

            } else {
                // Treat as positional argument

                // Do we have another one in our spec
                if (positionalArgumentI.hasNext()) {
                    // Grab it
                    currentPositionalArgument = positionalArgumentI.next();

                    // Set it
                    argumentValueMap.put(currentPositionalArgument, token);
                } else {
                    // Go we have a current one?
                    if (currentPositionalArgument != null) {
                        // Is is multiple?
                        if (currentPositionalArgument.getMultiplicity().equals(PositionalArgumentMultiplicity.ONE_OR_MANY) || currentPositionalArgument.getMultiplicity().equals(PositionalArgumentMultiplicity.ZERO_OR_MANY)) {
                            // Append to existing value
                            String currentValue = argumentValueMap.get(currentPositionalArgument);
                            String newValue = currentValue + " " + token;
                            argumentValueMap.put(currentPositionalArgument, newValue);
                        } else {
                            throw new ArgumentUserInputException("Too many parameters specified");
                        }
                    }
                }
            }
        }

        // Go through any remaining positional arguments
        while (positionalArgumentI.hasNext()) {
            currentPositionalArgument = positionalArgumentI.next();
            if (currentPositionalArgument.getMultiplicity().equals(PositionalArgumentMultiplicity.ONE) || currentPositionalArgument.getMultiplicity().equals(PositionalArgumentMultiplicity.ONE_OR_MANY)) {
                throw new ArgumentUserInputException("Not enough parameters specified");
            }
        }

        // Build our final result
        ParsedArguments parsedArguments = new ParsedArguments(argumentsSpecification, argumentValueMap);

        return parsedArguments;
    }

    public String getValueFor(ArgumentSpecification argumentSpecification) {
        return argumentValueMap.get(argumentSpecification);
    }

    public String getValueFor(String name) {
        for (ArgumentSpecification argumentSpecification : argumentValueMap.keySet()) {
            if (argumentSpecification.getName().equalsIgnoreCase(name)) {
                return getValueFor(argumentSpecification);
            }
        }

        return null;
    }

    public boolean getValueForBoolean(String name) {
        for (ArgumentSpecification argumentSpecification : argumentValueMap.keySet()) {
            if (argumentSpecification.getName().equalsIgnoreCase(name)) {
                return getBooleanValueFor(argumentSpecification);
            }
        }

        return false;
    }

    public boolean getBooleanValueFor(ArgumentSpecification flagArgumentSpecification) {
        String value = getValueFor(flagArgumentSpecification);
        if (value == null) {
            return false;
        }
        return Boolean.valueOf(value);
    }
}
