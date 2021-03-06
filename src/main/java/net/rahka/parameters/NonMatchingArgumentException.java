package net.rahka.parameters;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * An exception used to indicate that an argument supplied to a {@link CollectionFlag} or {@link MapFlag }does not
 * match any object.
 */
public class NonMatchingArgumentException extends ParameterException {

    public NonMatchingArgumentException(String msg, Collection<?> options) {
        super("Invalid option (" + msg + ")! Allowed options are {" + options.stream().map(Object::toString).collect(Collectors.joining(", ")) + "}!");
    }

}
