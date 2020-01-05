package net.rahka.parameters;

/**
 * An exception used to indicate that an exception was thrown when parsing an argument. The caught exception is put as
 * the cause of this exception and can be retrieved with the {@link Exception#getCause()} method.
 */
public class ExceptionCaughtException extends ParameterException {

    public ExceptionCaughtException(String msg, Exception cause) {
        super(msg, cause);
    }

}
