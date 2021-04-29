package net.rahka.parameters;

/**
 * A flag that retrieves a value from a {@link Supplier} if it the flag is present.
 * <p>
 * Any exceptions thrown when running will be wrapped inside a {@link ParameterException} that will be
 * then be thrown by {@link ParameterInterpreter#interpret(String[])}.
 */
public class SupplierFlag<T> extends Flag {

    private final Supplier<T> supplier;

    /**
     * @param name        the name of the flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param supplier    the consumer to be run if flag is present
     */
    public SupplierFlag(String name, String flag, String description, Supplier<T> supplier) {
        super(name, flag, description, false);
        this.supplier = supplier;
    }

    @Override
    protected boolean expectsArgument() {
        return false;
    }

    @Override
    protected Object parseArgument(String arg) throws Exception {
        return supplier.supply();
    }

    /**
     * Functional interface for use with this flag.
     */
    public interface Supplier<T> {

        T supply() throws Exception;

    }

}
