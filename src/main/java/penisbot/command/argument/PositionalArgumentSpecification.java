package penisbot.command.argument;

public class PositionalArgumentSpecification extends ArgumentSpecification {

    private final PositionalArgumentMultiplicity multiplicity;

    public PositionalArgumentSpecification(String name, String description, PositionalArgumentMultiplicity multiplicity) {
        super(name, description);

        this.multiplicity = multiplicity;
    }

    public PositionalArgumentMultiplicity getMultiplicity() {
        return multiplicity;
    }
}
