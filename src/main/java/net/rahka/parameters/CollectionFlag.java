package net.rahka.parameters;

import java.util.Collection;

/**
 * This class is a Flag that only accepts arguments that are contained in a collection. It uses {@link Object#toString}
 * in order to check for matches. The matching is case-sensitive.
 * <p>
 * If more than one object matches a argument then only the the first object encountered by the collection's
 * iterator is parsed.
 */
public class CollectionFlag<T> extends Flag {

    /**
     * A collection of values that this flag can have as an argument.
     */
    private Collection<T> collection;

    /**
     * @param name        the name of flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param collection  the list of accepted arguments
     * @param required    whether or not the flag is required
     */
    public CollectionFlag(String name, String flag, String description, Collection<T> collection, boolean required) {
        super(name, flag, description, required);
        this.collection = collection;
    }

    /**
     * @param name        the name of flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param collection  the list of accepted arguments
     */
    public CollectionFlag(String name, String flag, String description, Collection<T> collection) {
        this(name, flag, description, collection, false);
    }

    @Override
    public boolean expectsArgument() {
        return true;
    }

    @Override
    protected Object parseArgument(String arg) throws ParameterException {
        for (T t : collection) {
            if (t.toString().equals(arg)) {
                return t;
            }
        }

        throw new NonMatchingArgumentException(arg, collection);
    }

}