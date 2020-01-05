package net.rahka.parameters;

/**
 * A base class for all custom exceptions in the package
 */
public class ParameterException extends RuntimeException {

    public ParameterException(String string) {
        super(string);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

}
