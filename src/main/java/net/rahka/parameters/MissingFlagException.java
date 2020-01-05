package net.rahka.parameters;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * An exception used to indicate that one or more required flags were not supplied to a {@link ParameterInterpreter}
 */
public class MissingFlagException extends ParameterException {

    public MissingFlagException(Collection<Flag> flags) {
        super("Missing flags (" + flags.stream().map(Flag::getName).collect(Collectors.joining(", ")) + ")!");
    }

}
