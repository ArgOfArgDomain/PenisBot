package penisbot.command.argument;

public class ArgumentUserInputException extends ArgumentException {

    public ArgumentUserInputException() {
    }

    public ArgumentUserInputException(String message) {
        super(message);
    }

    public ArgumentUserInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentUserInputException(Throwable cause) {
        super(cause);
    }

    public ArgumentUserInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
