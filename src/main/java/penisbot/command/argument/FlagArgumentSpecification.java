package penisbot.command.argument;

public class FlagArgumentSpecification extends ArgumentSpecification {

    private final String shortName;
    private final boolean isBoolean;

    public FlagArgumentSpecification(String name, String shortName, String description, boolean isBoolean) {
        super(name, description);
        this.shortName = shortName;
        this.isBoolean = isBoolean;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isBoolean() {
        return isBoolean;
    }
}
