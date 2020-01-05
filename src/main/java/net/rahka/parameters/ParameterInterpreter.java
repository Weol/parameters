package net.rahka.parameters;

import java.util.*;

/**
 * This class provides argument handling in the form of strings. When constructed it can be given
 * a set of {@link Flag} which it will check for and parse. Any superfluous unnamed arguments (required arguments) or
 * flags that are not registered by this interpreter are silently ignored.
 * <p>
 * A prefix can be set when constructing a interpreter, if no prefix is supplied then "-" is used.
 * This prefix decides how the interpreter will differentiate between flags and their arguments, and between flags and
 * unnamed arguments (required arguments). The prefix will also be used to check whether a short-hand name or a
 * full name was used as a flag; if the prefix occurs twice in succession ({@code prefix + prefix}) then the
 * interpreter will check for a flag by its name instead of its short-hand name. For example if we have a interpreter
 * with a flag {@code new Flag("name", "n", "Description")} then we can pass "--name Erik" or "-n Erik", both of
 * these strings will set the flag.
 * <p>
 * Exceptions thrown during parsing will be wrapped inside a {@link ParameterException} that gets thrown by
 * {@link #interpret(String[])}.
 * <p>
 * If any required arguments are missing then a {@link MissingFlagException} will be thrown by
 * {@link #interpret(String[])}.
 */
public class ParameterInterpreter {

    private HashMap<String, Flag> flags;
    private LinkedList<Flag> required;

    private String prefix;

    /**
     * Constructs a new ParameterInterpreter that will check and parse the supplied flags. The prefix parameter decides
     * how the interpreter will differentiate between flags and their arguments, and between flags and unnamed
     * arguments (required arguments).
     */
    public ParameterInterpreter(String prefix, Flag... flags) {
        this.prefix = prefix;

        this.flags = new HashMap<>();
        this.required = new LinkedList<>();

        for (Flag flag : flags) {
            addFlag(flag);
        }
    }

    /**
     * Constructs a new ParameterInterpreter using the prefix "-" that will check and parse the supplied flags.
     */
    public ParameterInterpreter(Flag... flags) {
        this("-", flags);
    }

    /**
     * Adds a flag to this interpreter. If if the flag's short-hand name or name already exists in this interpreter
     * then they will be overridden. If the overridden flag is a required one then it will never be able to meet its
     * requirements and will always fail when interpreting arguments.
     */
    public void addFlag(Flag flag) {
        this.flags.put(flag.getName(), flag);
        this.flags.put(flag.getFlag(), flag);
        if (flag.isRequired()) {
            required.addLast(flag);
        }
    }

    public Collection<Flag> getFlags() {
        return flags.values();
    }

    /**
     * Interprets on array of parameters. Any superfluous unnamed arguments (required arguments) are silently ignored.
     * Any flags that are not registered by this interpreter are also silently ignored.
     *
     * @throws NonMatchingArgumentException if the argument of a {@link CollectionFlag} did not exist in its collection
     * @throws MissingFlagException         if a required flag is not present
     * @throws MissingArgumentException     if a flag that requires an argument did not have a argument
     * @throws ExceptionCaughtException     if a exception was thrown when parsing a flag
     */
    public ParameterInterpretation interpret(String[] args) {
        ParameterInterpretation interpretation = new ParameterInterpretation();
        interpretation.prepareRequired(required.toArray(new Flag[0]));

        ListIterator<String> iterator = Arrays.asList(args).listIterator();

        while (iterator.hasNext()) {
            String arg = iterator.next();
            if (arg.startsWith(prefix + prefix)) {
                arg = arg.substring(prefix.length() * 2);
                interpretFlag(interpretation, iterator, arg);
            } else if (arg.startsWith(prefix)) {
                arg = arg.substring(prefix.length());
                interpretFlag(interpretation, iterator, arg);
            } else if (!interpretation.required.isEmpty()) { //Ignore any superfluous unnamed parameters
                arg = interpretation.popRequired().getName();
                iterator.previous();
                interpretFlag(interpretation, iterator, arg);
            }
        }

        if (!interpretation.required.isEmpty()) {
            throw new MissingFlagException(interpretation.required);
        }

        return interpretation;
    }

    private void interpretFlag(ParameterInterpretation interpretation, Iterator<String> iterator, String arg) {
        if (flags.containsKey(arg)) {
            Flag flag = flags.get(arg);
            try {
                if (flag.expectsArgument()) {
                    if (!iterator.hasNext()) {
                        throw new MissingArgumentException("Missing argument for: " + prefix + arg);
                    }

                    interpretation.addFlag(flag, flag.parseArgument(iterator.next()));
                } else {
                    interpretation.addFlag(flag, flag.parseArgument(null));
                }
            } catch (ParameterException e) {
                throw e; //We dont want to wrap exceptions thrown by ourselves
            } catch (Exception e) {
                String msg = "Exception thrown when parsing argument for flag (" + flag.getName() + ")!";
                throw new ExceptionCaughtException(msg, e);
            }
        }
    }

}
