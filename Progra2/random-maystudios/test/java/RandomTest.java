import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class RandomTest {

    private static final Object[] SAME_ARG = {new String[]{"5", "5"}};
    private static final Object[] DICE_ARGS = {new String[]{"1", "6"}};
    private static final Object[] DICE10_ARGS = {new String[]{"10", "16"}};
    private static final Object[] THOUSAND_ARGS = {new String[]{"1000", "2000"}};

    private static final RuntimeClass RANDOM_CLASS = RuntimeClass.forName("Random");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(RANDOM_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static <T> String repeatedExecution(java.util.function.Supplier<T> supplier, int repetitions) {
        StringBuilder overallOutput = new StringBuilder();
        for(int i = 0; i < repetitions; i++) {
            var result = Program.execute(supplier);
            // assure that we also support `print` instead of `println`
            overallOutput.append(result.getOutput().trim()).append("\n");
        }
        return overallOutput.toString();
    }

    private int getAsInt(String line) {
        try {
            return Integer.parseInt(line);
        } catch(NumberFormatException e) {
            fail("Ausgabe einer Ganzzahl erwartet, aber »" + line + "« erhalten. (Geben Sie einen double aus?)");
        }
        return 1337;
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob immer die Zahl '5' ausgegeben wird, wenn sowohl die obere, als auch die untere Grenze gleich '5' sind.")
    void testBothArgs5() {
        var output = repeatedExecution(() -> MAIN_METHOD.invoke(null, SAME_ARG), 10);
        assertThat(output).hasLineCount(10);
        assertThat(output.lines()).allMatch(line -> line.equals("5") || line.equals("5.0"), "Ausgabe sollte nur »5« enthalten und nichts anderes.");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob für die Grenzen '1' und '6' (wie bei einem Würfel) auch immer Zahlen zwischen diesen Grenzen ausgegeben werden.")
    void testDice() {
        var output = repeatedExecution(() -> MAIN_METHOD.invoke(null, DICE_ARGS), 10);
        assertThat(output).hasLineCount(10);
        assertThat(output.lines()).allMatch(line -> Double.parseDouble(line) >= 1 && Double.parseDouble(line) <= 6);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob für die Grenzen '1000' und '2000' auch immer Zahlen zwischen diesen Grenzen ausgegeben werden und die Zahl nicht immer dieselbe ist.")
    void testThousand() {
        var output = repeatedExecution(() -> MAIN_METHOD.invoke(null, THOUSAND_ARGS), 1000);
        assertThat(output).hasLineCount(1000);
        assertThat(output.lines()).allMatch((line) -> getAsInt(line) >= 1000 && getAsInt(line) <= 2000);
        assertThat(Arrays.stream(output.split("\n")).collect(Collectors.toSet())).hasSizeGreaterThan(2);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die untere Grenze manchmal ausgegeben wird, manchmal aber auch eine andere Zahl; Würfel von 10 bis 16. (Hinweis: Es ist sehr unwahrscheinlich, dass dieser Test bei richtigem Code fehlschlägt, aber es ist möglich.)")
    void testLowerBoundIncluded() {
        var output = repeatedExecution(() -> MAIN_METHOD.invoke(null, DICE10_ARGS), 10000);
        assertThat(output).hasLineCount(10000);
        assertThat(output.lines()).withFailMessage("expected to get one random number which is 10 after 10000 repetitions").anyMatch((line) -> getAsInt(line) == 10);
        assertThat(output.lines()).withFailMessage("expected to get one random number which is not 10 after 10000 repetitions").anyMatch((line) -> getAsInt(line) != 10);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die obere Grenze manchmal ausgegeben wird; Würfel von 1 bis 6. (Hinweis: Es ist sehr unwahrscheinlich, dass dieser Test bei richtigem Code fehlschlägt, aber es ist möglich.)")
    void testUpperBoundIncluded() {
        var output = repeatedExecution(() -> MAIN_METHOD.invoke(null,DICE_ARGS), 10000);
        assertThat(output).hasLineCount(10000);
        assertThat(output.lines()).withFailMessage("expected to get one random number which is 6 after 10000 repetitions").anyMatch((line) -> getAsInt(line) == 6);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob auch Zahlen zwischen den Grenzen manchmal ausgegeben werden; Würfel von 1 bis 6. (Hinweis: Es ist sehr unwahrscheinlich, dass dieser Test bei richtigem Code fehlschlägt, aber es ist möglich.)")
    void testMiddleIncluded() {
        var output = repeatedExecution(() -> MAIN_METHOD.invoke(null,DICE_ARGS), 10000);
        assertThat(output).hasLineCount(10000);
        assertThat(output.lines()).withFailMessage("expected to get one random number which is 3 after 10000 repetitions").anyMatch((line) -> getAsInt(line) == 3);
    }
}
