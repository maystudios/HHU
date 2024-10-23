import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LottoTest {

    private static final RuntimeClass LOTTO_CLASS = RuntimeClass.forName("Lotto");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LOTTO_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] NO_ARGS = { new String[0] };
    private static final Object[] THREE_ARGS = { new String[]{"6", "49", "49"} };
    private static final Object[] ARGS_50_49 = { new String[]{"50", "49"} };
    private static final Object[] ARGS_0_49 = { new String[]{"0", "49"} };
    private static final Object[] ARGS_10_10 = { new String[]{"10", "10"} };
    private static final Object[] ARGS_6_49 = { new String[]{"6", "49"} };
    private static final Object[] ARGS_19_20 = { new String[]{"19", "20"} };

    private static final int REPETITIONS = 100;

    @Test
    @DisplayName("[1 Punkt] Bei 0 oder 3 Argumenten gibt es eine Fehlermeldung")
    void testErrorWrongNumberOfArguments() {
        var result0 = Program.execute(() -> MAIN_METHOD.invoke(null, NO_ARGS));
        assertThat(result0.getOutput()).contains("ERROR");

        var result3 = Program.execute(() -> MAIN_METHOD.invoke(null, THREE_ARGS));
        assertThat(result3.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Bei n>m oder n<1 gibt es eine Fehlermeldung")
    void testNInvalid() {
        var result50 = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_50_49));
        assertThat(result50.getOutput()).contains("ERROR");

        var result0 = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_0_49));
        assertThat(result0.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Bei 6 aus 49 sind die Zahlen echt zwischen 0 und 50")
    void testRange() {
        for(int i = 0; i < REPETITIONS; i++) {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_6_49));
            // note: split removes trailing empty strings
            int[] numbersDrawn = Stream.of(result.getOutput().lines().findFirst().orElse("").replaceAll(" ", "").split(",")).mapToInt(Integer::parseInt).toArray();
            assertThat(numbersDrawn).hasSize(6);
            for(int number: numbersDrawn) {
                assertThat(number).isGreaterThan(0);
                assertThat(number).isLessThan(50);
            }
        }
    }

    @Test
    @DisplayName("[1 Punkt] Ausgabe endet nicht mit Komma")
    void testNoTrailingComma() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_6_49));
        assertThat(result.getOutput().trim()).doesNotEndWith(",");
        assertThat(result.getOutput().trim()).contains(",");
        assertThat(result.getOutput().trim()).doesNotContain("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Ausgabe enthält die geforderten Leerzeichen")
    void testFormatCorrect() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_6_49));
        assertThat(result.getOutput().trim()).contains(", ");
        assertThat(result.getOutput().trim()).doesNotContain("ERROR");
        int[] numbersDrawn = Stream.of(result.getOutput().lines().findFirst().orElse("").split(", ")).mapToInt(Integer::parseInt).toArray();
        assertThat(numbersDrawn).hasSize(6);
    }

    @Test
    @DisplayName("[2 Punkte] Bei 19 aus 20 werden keine Zahlen doppelt gezogen")
    void testNInNContainsAllNumber() {
        for(int i = 0; i < REPETITIONS; i++) {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_19_20));
            Set<Integer> numbersDrawn = Stream.of(result.getOutput().lines().findFirst().orElse("").replaceAll(" ", "").split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toSet());
            assertThat(numbersDrawn).hasSize(19);
            for (int number : numbersDrawn) {
                assertThat(number).isGreaterThan(0);
                assertThat(number).isLessThan(21);
            }
        }
    }

    @Test
    @DisplayName("[1 Punkt] Bei 10 aus 10 werden auch 1 und 10 gezogen")
    void testMinMaxIncluded() {
        for(int i = 0; i < REPETITIONS; i++) {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_10_10));
            int[] numbersDrawn = Stream.of(result.getOutput().lines().findFirst().orElse("").replaceAll(" ", "").split(",")).mapToInt(Integer::parseInt).toArray();
            assertThat(numbersDrawn).contains(1, 10);
        }
    }
}
