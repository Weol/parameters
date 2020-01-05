import net.rahka.parameters.ExceptionCaughtException;
import net.rahka.parameters.Flag;
import net.rahka.parameters.FunctionFlag;
import net.rahka.parameters.MissingArgumentException;
import net.rahka.parameters.MissingFlagException;
import net.rahka.parameters.ParameterInterpretation;
import net.rahka.parameters.ParameterInterpreter;
import net.rahka.parameters.RunnableFlag;
import net.rahka.parameters.SupplierFlag;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class InterpreterTest {

    ParameterInterpreter interpreter;

    @Test(expected = MissingArgumentException.class)
    public void interpret_throwsMissingArgumentException_whenFlagArgumentIsNotSupplied() {
        interpreter = new ParameterInterpreter(
                new Flag("Simple flag", "s", "Simple flag description"),
                new FunctionFlag<>("Integer flag", "i", "Flag description", Integer::parseInt)
        );

        interpreter.interpret(new String[] {"-s", "-i"});
    }

    @Test(expected = MissingFlagException.class)
    public void interpret_throwsMissingFlagException_whenRequiredFlagIsNotSupplied() {
        interpreter = new ParameterInterpreter(
                new FunctionFlag<>("Simple flag", "s", "Simple flag description", Integer::parseInt, true),
                new FunctionFlag<>("Integer flag", "i", "Flag description", Integer::parseInt)
        );

        interpreter.interpret(new String[] {"-i", "4"});
    }

    @Test(expected = ExceptionCaughtException.class)
    public void interpret_throwsExceptionCaughtException_whenParsingThrowsException() {
        RunnableFlag.Runnable runnable = () -> {throw new TestException();};

        interpreter = new ParameterInterpreter(
                new RunnableFlag("Runnable flag", "i", "Flag description", runnable)
        );

        interpreter.interpret(new String[] {"-i"});
    }

    @Test(expected = TestException.class)
    public void interpret_throwsExceptionCaughtException_withCorrectCause() throws Throwable {
        RunnableFlag.Runnable runnable = () -> {throw new TestException();};

        interpreter = new ParameterInterpreter(
                new RunnableFlag("Runnable flag", "i", "Flag description", runnable)
        );

        try {
            interpreter.interpret(new String[] {"-i"});
        } catch (ExceptionCaughtException e) {
            throw e.getCause();
        }
    }

    @Test
    public void interpreter_lastAddedFlagIsUsed_whenFlagsWithDuplicateNamesAreSupplied() {
        interpreter = new ParameterInterpreter(
                new SupplierFlag<>("number", "n", "Flag description", () -> 1),
                new SupplierFlag<>("name", "n", "Flag description", () -> true)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"-n"});

        assertEquals(true, interpretation.get("name"));
        assertNull(interpretation.get("number"));
    }

    @Test
    public void interpreter_parsesUnnamedParameters() {
        interpreter = new ParameterInterpreter(
                new FunctionFlag<>("int", "i", "Simple flag description", Integer::parseInt, true),
                new FunctionFlag<>("double", "d", "Flag description", Double::parseDouble),
                new FunctionFlag<>("int2", "i2", "Flag description", Integer::parseInt, true),
                new FunctionFlag<>("float", "f", "Flag description", Float::parseFloat),
                new FunctionFlag<>("boolean", "bool", "Flag description", Boolean::parseBoolean, true)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"4", "-f" , "9.2", "5", "true"});

        assertEquals(4, interpretation.get("int"));
        assertEquals(5, interpretation.get("int2"));
        assertEquals(9.2f, interpretation.get("float"));
        assertEquals(true, interpretation.get("boolean"));
    }

    @Test
    public void interpretation_get() {
        interpreter = new ParameterInterpreter(
                new SupplierFlag<>("number", "n", "Flag description", () -> null),
                new SupplierFlag<>("name", "a", "Flag description", () -> true),
                new SupplierFlag<>("ok", "k", "Flag description", () -> 44)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"-n", "-k"});

        assertNull(interpretation.get("number"));
        assertNull(interpretation.get("nonexisting"));
        assertEquals(44, interpretation.get("ok"));
    }

    @Test
    public void interpretation_get_orDefault() {
        interpreter = new ParameterInterpreter(
                new SupplierFlag<>("number", "n", "Flag description", () -> 2),
                new SupplierFlag<>("name", "a", "Flag description", () -> true),
                new SupplierFlag<>("ok", "k", "Flag description", () -> 44)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"-k"});

        assertEquals(4, (int) interpretation.get("number", 4));
        assertNull(interpretation.get("nonexisting", null));
        assertEquals(44, (int) interpretation.get("ok", 11));
    }

    @Test
    public void interpretation_has_returnsFalse() {
        interpreter = new ParameterInterpreter(
                new SupplierFlag<>("number", "n", "Flag description", () -> 4),
                new SupplierFlag<>("name", "a", "Flag description", () -> 1)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"-a"});

        assertFalse(interpretation.has("number"));
        assertFalse(interpretation.has("nonexistant"));
    }

    @Test
    public void interpretation_has_returnsTrue() {
        interpreter = new ParameterInterpreter(
                new SupplierFlag<>("number", "n", "Flag description", () -> 4),
                new SupplierFlag<>("name", "a", "Flag description", () -> 1)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"-a", "-n"});

        assertTrue(interpretation.has("name"));
        assertTrue(interpretation.has("number"));
    }

    private static class TestException extends Exception {

    }

}
