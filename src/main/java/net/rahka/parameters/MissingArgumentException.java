package net.rahka.parameters;

/**
 * An exception used to indicate that a flag that requires an argument had no argument
 */
public class MissingArgumentException extends ParameterException {

    public MissingArgumentException(String msg) {
        super(msg);
    }

}
