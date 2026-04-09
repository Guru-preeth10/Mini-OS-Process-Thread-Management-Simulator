package os.exception;

/**
 * Custom Exception class for invalid process operations
 * Demonstrates exception handling in OOP
 */
public class InvalidProcessException extends Exception {

    /**
     * Constructor with error message
     * @param message The error message
     */
    public InvalidProcessException(String message) {
        super(message);
    }

    /**
     * Constructor with custom message and cause
     * @param message The error message
     * @param cause The cause of the exception
     */
    public InvalidProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
