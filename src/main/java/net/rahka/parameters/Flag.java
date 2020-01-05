package net.rahka.parameters;

/**
 * This class defines a parameter with a name, short-hand name, and description. It is used
 * by {@link ParameterInterpreter} and {@link ParameterInterpretation} to handle the parameters.
 * <p>
 * It is the base class of all other flags, but can be used by itself to represent a boolean flag that requires to
 * argument, its only significance is whether or not it is present.
 */
public class Flag {

    /**
     * The name of the flag. This is the name that is used to retrieve the parsed argument from
     * {@link ParameterInterpretation}.
     */
    private String name;

    /**
     * The short-hand name for this flag. This is what is usually used in the command line.
     * For example -f instead of --force
     */
    private String flag;

    /**
     * The description of this flag. This is used by the help command in order to tell the user what
     * this flag is.
     */
    private String description;

    /**
     * Whether or not this flag is required (non-optional).
     */
    private boolean required;

    /**
     * Package private because this should only be used by sub-classes. It makes no sense to have required flags of
     * this type, then it always has to be there.
     *
     * @param name        the name of the flag
     * @param flag        the short-hand name
     * @param description the description
     * @param required    whether or not this flag is required.
     */
    Flag(String name, String flag, String description, boolean required) {
        this.name = name;
        this.flag = flag;
        this.description = description;
        this.required = required;
    }

    /**
     * @param name        the name of the flag
     * @param flag        the short-hand name
     * @param description the description
     */
    public Flag(String name, String flag, String description) {
        this(name, flag, description, false);
    }

    /**
     * Used by {@link ParameterInterpreter} to check if the flag requires on argument
     */
    protected boolean expectsArgument() {
        return false;
    }

    /**
     * Parses this flag's argument and returns its parsed value
     *
     * @return
     */
    protected Object parseArgument(String arg) throws Exception {
        return null;
    }

    public boolean isRequired() {
        return required;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public String getDescription() {
        return description;
    }

}
