package net.rahka.parameters;

/**
 * A flag that requires a argument and parses it into a specific type. It runs a
 * {@link Function} in order to parse the argument.
 * <p>
 * Any exceptions thrown when parsing the argument will be wrapped inside a {@link ParameterException} that will be
 * then be thrown by {@link ParameterInterpreter#interpret(String[])}.
 */
public class FunctionFlag<T> extends Flag {

    private final Function<T> parser;

    /**
     * @param name        the name of the flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param parser      the parsing function of the flag
     * @param required    whether or not this flag is required
     */
    public FunctionFlag(String name, String flag, String description, Function<T> parser, boolean required) {
        super(name, flag, description, required);
        this.parser = parser;
    }

    /**
     * @param name        the name of the flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param parser      the parsing function of the flag
     */
    public FunctionFlag(String name, String flag, String description, Function<T> parser) {
        this(name, flag, description, parser, false);
    }

    @Override
    protected boolean expectsArgument() {
        return true;
    }

    @Override
    protected Object parseArgument(String arg) throws Exception {
        return parser.parse(arg);
    }

    /**
     * Functional interface for use with this flag.
     */
    public interface Function<T> {

        T parse(String argument) throws Exception;

    }

}
