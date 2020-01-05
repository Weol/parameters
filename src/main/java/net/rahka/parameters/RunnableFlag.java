package net.rahka.parameters;

/**
 * A flag that runs a {@link Runnable} if it the flag is present. It will produce a null value that will be
 * returned by {@link ParameterInterpretation#get(String)}
 * <p>
 * Any exceptions thrown when running will be wrapped inside a {@link ParameterException} that will be
 * then be thrown by {@link ParameterInterpreter#interpret(String[])}.
 */
public class RunnableFlag extends Flag {

    private Runnable runnable;

    /**
     * @param name        the name of the flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param runnable    the consumer to be run if flag is present
     */
    public RunnableFlag(String name, String flag, String description, Runnable runnable) {
        super(name, flag, description, false);
        this.runnable = runnable;
    }

    @Override
    protected boolean expectsArgument() {
        return false;
    }

    @Override
    protected Object parseArgument(String arg) throws Exception {
        runnable.run();
        return null;
    }

    /**
     * Functional interface for use with this flag.
     */
    public interface Runnable {

        void run() throws Exception;

    }

}
