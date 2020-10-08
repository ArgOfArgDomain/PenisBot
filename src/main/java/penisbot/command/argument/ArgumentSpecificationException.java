package penisbot.command.argument;

/**
 * Represents an error in the Argument Specification itself.
 */
public class ArgumentSpecificationException extends ArgumentException {

    public ArgumentSpecificationException() {
    }

    public ArgumentSpecificationException(String message) {
        super(message);
    }

    public ArgumentSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentSpecificationException(Throwable cause) {
        super(cause);
    }

    public ArgumentSpecificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
