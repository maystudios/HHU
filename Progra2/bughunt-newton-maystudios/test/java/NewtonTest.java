import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class NewtonTest {

    private static final RuntimeClass NEWTON_CLASS = RuntimeClass.forName("Newton");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(NEWTON_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] NO_ARG = { new String[]{} };
    private static final Object[] TWO_ARGS = { new String[]{"9", "9"} };
    private static final Object[] ZERO_ARG = { new String[]{"0"} };
    private static final Object[] FOUR_ARG = { new String[]{"4"} };
    private static final Object[] TWO_ARG = { new String[]{"2"} };
    private static final Object[] FRAC_ARG = { new String[]{"123.4"} };

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei keinem Argument eine Fehlermeldung ausgegeben wird.")
    void testEmptyArgs() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NO_ARG), "");
        assertThat(result.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei zwei Argumenten eine Fehlermeldung ausgegeben wird und nichts berechnet wird.")
    void testTwoArgs() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, TWO_ARGS), "");
        assertThat(result.getOutput()).contains("ERROR");
        assertThat(result.getOutput()).doesNotContain("3");
        assertThat(result.getOutput()).doesNotContain("2");
    }

    private static double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            Assertions.fail("»" + s + "« cannot be parsed as double");
        }
        return -1;
    }
    
    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Wurzel von 0 korrekt berechnet wird.")
    void testRoot0() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ZERO_ARG), "");
        assertThat(result.getOutput().lines().mapToDouble(NewtonTest::parseDoubleSafe).findFirst().orElse(-1)).isCloseTo(0, Offset.offset(0.0001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Wurzel von 4 korrekt berechnet wird.")
    void testRoot4() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, FOUR_ARG), "");
        assertThat(result.getOutput().lines().mapToDouble(NewtonTest::parseDoubleSafe).findFirst().orElse(-1)).isCloseTo(2.0, Offset.offset(0.0001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Wurzel von 2 korrekt berechnet wird.")
    void testRoot2() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, TWO_ARG), "");
        assertThat(result.getOutput().lines().mapToDouble(NewtonTest::parseDoubleSafe).findFirst().orElse(-1)).isCloseTo(Math.sqrt(2), Offset.offset(0.0001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Wurzel von 123,4 korrekt berechnet wird.")
    void testRoot123Comma4() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, FRAC_ARG), "");
        assertThat(result.getOutput().lines().mapToDouble(NewtonTest::parseDoubleSafe).findFirst().orElse(-1)).isCloseTo(Math.sqrt(123.4), Offset.offset(0.0001));
    }
}
