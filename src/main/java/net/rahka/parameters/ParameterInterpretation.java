package net.rahka.parameters;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class is constructed by {@link ParameterInterpreter} and contains the parsed arguments of whatever was parsed.
 */
public class ParameterInterpretation {

    HashMap<String, Object> flags;
    LinkedList<Flag> required;

    /**
     * Only {@link ParameterInterpreter} should be able to create interpretations.
     */
    ParameterInterpretation() {
        required = new LinkedList<>();
        flags = new HashMap<>();
    }

    /**
     * Returns the parsed value of a parameter if the parameter is present. Returns null if no matching flag is present
     * or in cases where a flag has parsed its argument to null for some reason. Use {@link #has(String)} before
     * calling this or use the {@link #get(String, Object)} in order to avoid this.
     *
     * @throws ClassCastException if the parsed object cannot be cast to the inferred type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) flags.get(name);
    }

    /**
     * Returns the parsed value of a parameter, if no parameter with the given name is not present then
     * the alternate object will be returned. If the parsed value of the flag is null, then null will be returned
     * instead of the alternate object.
     *
     * @throws ClassCastException if the parsed object cannot be cast to the type of the alternate object
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name, T alt) {
        if (has(name)) {
            return (T) flags.get(name);
        } else {
            return alt;
        }
    }

    /**
     * Checks whether or not a parameter is present in this interpretation
     */
    public boolean has(String string) {
        return flags.containsKey(string);
    }

    /**
     * Used by {@link ParameterInterpreter} to add parsed flags to the interpretation. The object may be null, in
     * that case the {@link #get(String)} and {@link #get(String, Object)} will also return null for the same flag.
     */
    void addFlag(Flag flag, Object object) {
        flags.put(flag.getName(), object);
        required.remove(flag);
    }

    /**
     * Used by {@link ParameterInterpreter} to add the interpreter's required flags
     */
    void prepareRequired(Flag[] flags) {
        for (Flag flag : flags) {
            required.addLast(flag);
        }
    }

    /**
     * Used by {@link ParameterInterpreter} to pop a required flag from the interpretation
     */
    Flag popRequired() {
        return required.removeFirst();
    }

}
