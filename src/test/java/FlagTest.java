import net.rahka.parameters.CollectionFlag;
import net.rahka.parameters.ConsumerFlag;
import net.rahka.parameters.FunctionFlag;
import net.rahka.parameters.NonMatchingArgumentException;
import net.rahka.parameters.ParameterInterpretation;
import net.rahka.parameters.ParameterInterpreter;
import net.rahka.parameters.RunnableFlag;
import net.rahka.parameters.SupplierFlag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class FlagTest {

    @Test
    public void functionFlag_runsParseMethod_once() throws Exception {
        FunctionFlag.Function<?> function = mock(FunctionFlag.Function.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new FunctionFlag<>("Test name", "t", "Test description", function)
        );

        interpreter.interpret(new String[] {"-t", "testinput"});
        verify(function, times(1)).parse("testinput");
    }

    @Test
    public void consumerFlag_runsConsumeMethod_once() throws Exception {
        ConsumerFlag.Consumer consumer = mock(ConsumerFlag.Consumer.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new ConsumerFlag("Test name", "t", "Test description", consumer)
        );

        interpreter.interpret(new String[] {"-t", "testinput"});
        verify(consumer, times(1)).consume("testinput");
    }

    @Test
    public void runnableFlag_runsRunMethod_once() throws Exception {
        RunnableFlag.Runnable runnable = mock(RunnableFlag.Runnable.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new RunnableFlag("Test name", "t", "Test description", runnable)
        );

        interpreter.interpret(new String[] {"-t"});
        verify(runnable, times(1)).run();
    }

    @Test
    public void supplierFlag_runsSupplyMethod_once() throws Exception {
        SupplierFlag.Supplier<?> supplier = mock(SupplierFlag.Supplier.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new SupplierFlag<>("Test name", "t", "Test description", supplier)
        );

        interpreter.interpret(new String[] {"-t"});
        verify(supplier, times(1)).supply();
    }

    @Test
    public void collectionFlag_mapsItems() throws Exception {
        List<TestObj> collection = Arrays.asList(
                new TestObj("test1"),
                new TestObj("test2"),
                new TestObj("test3"),
                new TestObj("test4"),
                new TestObj("test5"));

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new CollectionFlag<>("Test name 1", "t2", "Test description", collection),
                new CollectionFlag<>("Test name 2", "t4", "Test description", collection)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"-t2", "test2", "-t4", "test4"});
        assertEquals(collection.get(1), interpretation.get("Test name 1"));
        assertEquals(collection.get(3), interpretation.get("Test name 2"));
    }

    @Test(expected = NonMatchingArgumentException.class)
    public void collectionFlag_throwsNonMatchingArgumentException_whenNonMatchingArgumentIsSupplied() throws Exception {
        List<TestObj> collection = Arrays.asList(
                new TestObj("test1"),
                new TestObj("test2"),
                new TestObj("test3"),
                new TestObj("test4"),
                new TestObj("test5"));

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new CollectionFlag<>("Test name 1", "t2", "Test description", collection),
                new CollectionFlag<>("Test name 2", "t4", "Test description", collection)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[] {"-t2", "test2", "-t4", "test0"});
        assertEquals(collection.get(1), interpretation.get("Test name 1"));
        assertEquals(collection.get(3), interpretation.get("Test name 2"));
    }

    private static class TestObj {

        public TestObj(String argument) {
            this.argument = argument;
        }

        private String argument;

        @Override
        public String toString() {
            return argument;
        }

    }

}
