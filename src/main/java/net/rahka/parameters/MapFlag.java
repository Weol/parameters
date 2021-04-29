package net.rahka.parameters;

import java.util.Map;

/**
 * This class is a Flag that only accepts arguments that are keys in a map. It iterates throught the map's
 * keys and checks if the key matches the argument. It uses {@link Object#toString} in order to check for matches.
 * The matching is case-sensitive.
 * <p>
 * If more than one object matches a argument then only the the first object encountered by the collection's
 * iterator is parsed.
 */
public class MapFlag<T> extends Flag {

    /**
     * A map of key that this flag can have as an argument and their corresponding values.
     */
    private final Map<?, T> map;

    /**
     * @param name        the name of flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param map         the map of accepted arguments
     * @param required    whether or not the flag is required
     */
    public MapFlag(String name, String flag, String description, Map<?, T> map, boolean required) {
        super(name, flag, description, required);
        this.map = map;
    }

    /**
     * @param name        the name of flag
     * @param flag        the short-hand name of the flag
     * @param description the description of the flag
     * @param map         the map of accepted arguments
     */
    public MapFlag(String name, String flag, String description, Map<?, T> map) {
        this(name, flag, description, map, false);
    }

    @Override
    public boolean expectsArgument() {
        return true;
    }

    @Override
    protected Object parseArgument(String arg) throws ParameterException {
        for (Object key : map.keySet()) {
            if (key.toString().equals(arg)) {
                return map.get(key);
            }
        }

        throw new NonMatchingArgumentException(arg, map.keySet());
    }

}