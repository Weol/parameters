package net.rahka.parameters;

/**
 * A flag that accepts a argument and calls a {@link Consumer} with the argument.
 * It will produce a null value that will be returned by {@link ParameterInterpretation#get(String)}.
 * <p>
 * Any exceptions thrown when consuming the argument will be wrapped inside a {@link ParameterException} that will be
 * then be thrown by {@link ParameterInterpreter#interpret(String[])}.
 */
public class ConsumerFlag extends Flag {

    private Consumer consumer;

    /**
     * @param name        the name of the flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param consumer    the consumer to be run if flag is present
     */
    public ConsumerFlag(String name, String flag, String description, Consumer consumer) {
        super(name, flag, description, false);
        this.consumer = consumer;
    }

    @Override
    protected boolean expectsArgument() {
        return true;
    }

    @Override
    protected Object parseArgument(String arg) throws Exception {
        consumer.consume(arg);
        return null;
    }

    /**
     * Functional interface for use with this flag.
     */
    public interface Consumer {

        void consume(String argument) throws Exception;

    }

}
