import net.rahka.parameters.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class FlagTest {

    @Test
    public void functionFlag_runsParseMethod_once() throws Exception {
        FunctionFlag.Function<?> function = mock(FunctionFlag.Function.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new FunctionFlag<>("Test name", "t", "Test description", function)
        );

        interpreter.interpret(new String[]{"-t", "testinput"});
        verify(function, times(1)).parse("testinput");
    }

    @Test
    public void consumerFlag_runsConsumeMethod_once() throws Exception {
        ConsumerFlag.Consumer consumer = mock(ConsumerFlag.Consumer.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new ConsumerFlag("Test name", "t", "Test description", consumer)
        );

        interpreter.interpret(new String[]{"-t", "testinput"});
        verify(consumer, times(1)).consume("testinput");
    }

    @Test
    public void runnableFlag_runsRunMethod_once() throws Exception {
        RunnableFlag.Runnable runnable = mock(RunnableFlag.Runnable.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new RunnableFlag("Test name", "t", "Test description", runnable)
        );

        interpreter.interpret(new String[]{"-t"});
        verify(runnable, times(1)).run();
    }

    @Test
    public void supplierFlag_runsSupplyMethod_once() throws Exception {
        SupplierFlag.Supplier<?> supplier = mock(SupplierFlag.Supplier.class);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new SupplierFlag<>("Test name", "t", "Test description", supplier)
        );

        interpreter.interpret(new String[]{"-t"});
        verify(supplier, times(1)).supply();
    }

    @Test
    public void collectionFlag_mapsItems() {
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

        ParameterInterpretation interpretation = interpreter.interpret(new String[]{"-t2", "test2", "-t4", "test4"});
        assertEquals(collection.get(1), interpretation.get("Test name 1"));
        assertEquals(collection.get(3), interpretation.get("Test name 2"));
    }

    @Test
    public void mapFlag_mapsItems() {
        Map<TestObj, Integer> map = new HashMap<>();

        TestObj obj2 = new TestObj("test2");
        TestObj obj4 = new TestObj("test4");

        map.put(new TestObj("test1"), 1);
        map.put(obj2, 2);
        map.put(new TestObj("test3"), 3);
        map.put(obj4, 4);
        map.put(new TestObj("test5"), 5);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new MapFlag<>("Test name 1", "t2", "Test description", map),
                new MapFlag<>("Test name 2", "t4", "Test description", map)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[]{"-t2", "test2", "-t4", "test4"});
        assertEquals(map.get(obj2), interpretation.get("Test name 1"));
        assertEquals(map.get(obj4), interpretation.get("Test name 2"));
    }


    @Test(expected = NonMatchingArgumentException.class)
    public void collectionFlag_throwsNonMatchingArgumentException_whenNonMatchingArgumentIsSupplied() {
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

        ParameterInterpretation interpretation = interpreter.interpret(new String[]{"-t2", "test2", "-t4", "test0"});
        assertEquals(collection.get(1), interpretation.get("Test name 1"));
        assertEquals(collection.get(3), interpretation.get("Test name 2"));
    }

    @Test(expected = NonMatchingArgumentException.class)
    public void mapFlag_throwsNonMatchingArgumentException_whenNonMatchingArgumentIsSupplied() {
        Map<TestObj, Integer> map = new HashMap<>();

        TestObj obj2 = new TestObj("test2");
        TestObj obj4 = new TestObj("test4");

        map.put(new TestObj("test1"), 1);
        map.put(obj2, 2);
        map.put(new TestObj("test3"), 3);
        map.put(obj4, 4);
        map.put(new TestObj("test5"), 5);

        ParameterInterpreter interpreter = new ParameterInterpreter(
                new MapFlag<>("Test name 1", "t2", "Test description", map),
                new MapFlag<>("Test name 2", "t4", "Test description", map)
        );

        ParameterInterpretation interpretation = interpreter.interpret(new String[]{"-t2", "test2", "-t4", "test0"});
        assertEquals(map.get(obj2), interpretation.get("Test name 1"));
        assertEquals(map.get(obj4), interpretation.get("Test name 2"));
    }

    private static class TestObj {

        private String argument;

        public TestObj(String argument) {
            this.argument = argument;
        }

        @Override
        public String toString() {
            return argument;
        }

    }

}
