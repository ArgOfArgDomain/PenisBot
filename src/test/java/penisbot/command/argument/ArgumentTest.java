package penisbot.command.argument;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentTest {


    @Test
    public void singlePositionalArgumentParsed() throws ArgumentUserInputException, ArgumentSpecificationException {

        // Single required positional argument "test1"
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("bleh");

        // Verify success
        assertEquals("bleh", parsedArguments.getValueFor(positionalArgument1Specification));

    }

    @Test
    public void multiplePositionalArgumentParsed() throws ArgumentUserInputException, ArgumentSpecificationException {

        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument2Specification = new PositionalArgumentSpecification("test2", "test 2", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument3Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ONE);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument2Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument3Specification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("bleh blah bloh");

        // Verify success
        assertEquals("bleh", parsedArguments.getValueFor(positionalArgument1Specification));
        assertEquals("blah", parsedArguments.getValueFor(positionalArgument2Specification));
        assertEquals("bloh", parsedArguments.getValueFor(positionalArgument3Specification));

    }

    @Test
    public void oneOrManyPositionalArgumentParsed() throws ArgumentUserInputException, ArgumentSpecificationException {

        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument2Specification = new PositionalArgumentSpecification("test2", "test 2", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument3Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ONE_OR_MANY);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument2Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument3Specification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("bleh blah bloh feh");

        // Verify success
        assertEquals("bleh", parsedArguments.getValueFor(positionalArgument1Specification));
        assertEquals("blah", parsedArguments.getValueFor(positionalArgument2Specification));
        assertEquals("bloh feh", parsedArguments.getValueFor(positionalArgument3Specification));

    }

    @Test
    public void notEnoughPositionalArgumentsGivenCausesException() throws ArgumentUserInputException, ArgumentSpecificationException {

        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument2Specification = new PositionalArgumentSpecification("test2", "test 2", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument3Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ONE_OR_MANY);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument2Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument3Specification);

        // Parse
        try {
            ParsedArguments parsedArguments = argumentsSpecification.parse("bleh blah");
            fail("Did not throw expected exception");
        } catch (ArgumentUserInputException e) {
            // Good!
        }
    }

    @Test
    public void tooManyPositionalArgumentsGivenCausesException() throws ArgumentUserInputException, ArgumentSpecificationException {

        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument2Specification = new PositionalArgumentSpecification("test2", "test 2", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument3Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ONE);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument2Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument3Specification);

        // Parse
        try {
            ParsedArguments parsedArguments = argumentsSpecification.parse("bleh blah bloh feh");
            fail("Did not throw expected exception");
        } catch (ArgumentUserInputException e) {
            // Good!
        }
    }

    @Test
    public void optionalPositionalArgumentsNotSpecified() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument2Specification = new PositionalArgumentSpecification("test2", "test 2", PositionalArgumentMultiplicity.ZERO_OR_ONE);
        PositionalArgumentSpecification positionalArgument3Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ZERO_OR_MANY);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument2Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument3Specification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("bleh");

        // Verify success
        assertEquals("bleh", parsedArguments.getValueFor(positionalArgument1Specification));
        assertNull(parsedArguments.getValueFor(positionalArgument2Specification));
        assertNull(parsedArguments.getValueFor(positionalArgument3Specification));
    }

    @Test
    public void zeroOrManyOptionalPositionalArgumentsParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ZERO_OR_MANY);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("bleh blah bloh feh");

        // Verify success
        assertEquals("bleh blah bloh feh", parsedArguments.getValueFor(positionalArgument1Specification));
    }

    @Test
    public void zeroOrOneAndZeroOrManyOptionalPositionalArgumentsParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument2Specification = new PositionalArgumentSpecification("test2", "test 2", PositionalArgumentMultiplicity.ZERO_OR_ONE);
        PositionalArgumentSpecification positionalArgument3Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ZERO_OR_MANY);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument2Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument3Specification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("bleh blah bloh feh");

        // Verify success
        assertEquals("bleh", parsedArguments.getValueFor(positionalArgument1Specification));
        assertEquals("blah", parsedArguments.getValueFor(positionalArgument2Specification));
        assertEquals("bloh feh", parsedArguments.getValueFor(positionalArgument3Specification));

    }

    @Test
    public void flagArgumentParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        FlagArgumentSpecification flagArgumentSpecification = new FlagArgumentSpecification("test1", "t", "test 1", false);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("--test1=blah");

        // Verify success
        assertEquals("blah", parsedArguments.getValueFor(flagArgumentSpecification));
    }

    @Test
    public void flagArgumentShortNameParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        FlagArgumentSpecification flagArgumentSpecification = new FlagArgumentSpecification("test1", "t", "test 1", false);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("-t=blah");

        // Verify success
        assertEquals("blah", parsedArguments.getValueFor(flagArgumentSpecification));
    }

    @Test
    public void flagArgumentSpaceDelimiatedValueParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        FlagArgumentSpecification flagArgumentSpecification = new FlagArgumentSpecification("test1", "t", "test 1", false);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("--test1 blah");

        // Verify success
        assertEquals("blah", parsedArguments.getValueFor(flagArgumentSpecification));
    }

    @Test
    public void flagArgumentShortNameSpaceDelimiatedValueParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        FlagArgumentSpecification flagArgumentSpecification = new FlagArgumentSpecification("test1", "t", "test 1", false);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("-t blah");

        // Verify success
        assertEquals("blah", parsedArguments.getValueFor(flagArgumentSpecification));
    }

    @Test
    public void booleanFlagArgumentParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        FlagArgumentSpecification flagArgumentSpecification = new FlagArgumentSpecification("test1", "t", "test 1", true);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("--test1");

        // Verify success
        assertTrue(parsedArguments.getBooleanValueFor(flagArgumentSpecification));
    }

    @Test
    public void booleanFlagArgumentShortNameParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        FlagArgumentSpecification flagArgumentSpecification = new FlagArgumentSpecification("test1", "t", "test 1", true);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("-t");

        // Verify success
        assertTrue(parsedArguments.getBooleanValueFor(flagArgumentSpecification));
    }

    @Test
    public void booleanFlagArgumentDefaultsToFalse() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        FlagArgumentSpecification flagArgumentSpecification = new FlagArgumentSpecification("test1", "t", "test 1", true);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse("");

        // Verify success
        assertFalse(parsedArguments.getBooleanValueFor(flagArgumentSpecification));
    }

    @Test
    public void mixtureOfFlagsParsed() throws ArgumentSpecificationException, ArgumentUserInputException {
        // Test a bunch of permutations
        mixtureOfFlagsParsed("bleh blah bloh feh --test4 --test5=dah --test6=roh");
        mixtureOfFlagsParsed("bleh blah bloh feh --test4 --test5 dah --test6 roh");
        mixtureOfFlagsParsed("bleh blah bloh feh -a -b=dah -c=roh");
        mixtureOfFlagsParsed("bleh blah bloh feh -a -b dah -c roh");
        mixtureOfFlagsParsed("bleh    blah    bloh    feh    --test4    --test5    dah    --test6     roh");
        mixtureOfFlagsParsed("--test4 --test5=dah --test6=roh bleh blah bloh feh");
        mixtureOfFlagsParsed("--test4 --test5 dah --test6 roh bleh blah bloh feh");
        mixtureOfFlagsParsed("-a -b=dah -c=roh bleh blah bloh feh");
        mixtureOfFlagsParsed("-a -b dah -c roh bleh blah bloh feh");
        mixtureOfFlagsParsed("--test4    --test5    dah    --test6     roh   bleh    blah    bloh    feh  ");
        mixtureOfFlagsParsed("bleh --test4 blah --test5=dah bloh --test6 roh feh");
        mixtureOfFlagsParsed("bleh --test4 blah --test5 dah bloh --test6=roh feh");
        mixtureOfFlagsParsed("bleh --test4 blah --test5 dah bloh feh --test6=roh");
    }

    public void mixtureOfFlagsParsed(String input) throws ArgumentSpecificationException, ArgumentUserInputException {

        // Build our argument specification
        ArgumentsSpecification argumentsSpecification = new ArgumentsSpecification();
        PositionalArgumentSpecification positionalArgument1Specification = new PositionalArgumentSpecification("test1", "test 1", PositionalArgumentMultiplicity.ONE);
        PositionalArgumentSpecification positionalArgument2Specification = new PositionalArgumentSpecification("test2", "test 2", PositionalArgumentMultiplicity.ZERO_OR_ONE);
        PositionalArgumentSpecification positionalArgument3Specification = new PositionalArgumentSpecification("test3", "test 3", PositionalArgumentMultiplicity.ZERO_OR_MANY);
        argumentsSpecification.addPositionalArgument(positionalArgument1Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument2Specification);
        argumentsSpecification.addPositionalArgument(positionalArgument3Specification);
        FlagArgumentSpecification flagArgumentSpecification1 = new FlagArgumentSpecification("test4", "a", "test 4", true);
        FlagArgumentSpecification flagArgumentSpecification2 = new FlagArgumentSpecification("test5", "b", "test 5", false);
        FlagArgumentSpecification flagArgumentSpecification3 = new FlagArgumentSpecification("test6", "c", "test 6", false);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification1);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification2);
        argumentsSpecification.addFlagArgument(flagArgumentSpecification3);

        // Parse
        ParsedArguments parsedArguments = argumentsSpecification.parse(input);

        // Verify success
        assertEquals("bleh", parsedArguments.getValueFor(positionalArgument1Specification));
        assertEquals("blah", parsedArguments.getValueFor(positionalArgument2Specification));
        assertEquals("bloh feh", parsedArguments.getValueFor(positionalArgument3Specification));
        assertTrue(parsedArguments.getBooleanValueFor(flagArgumentSpecification1));
        assertEquals("dah", parsedArguments.getValueFor(flagArgumentSpecification2));
        assertEquals("roh", parsedArguments.getValueFor(flagArgumentSpecification3));
    }

}



