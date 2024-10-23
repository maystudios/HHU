import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CosinusTest {

    private static final RuntimeClass COSINUS_CLASS = RuntimeClass.forName("Cosinus");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(COSINUS_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] NO_ARGS = { new String[0] };
    private static final Object[] NEGATIVE_ARG = { new String[]{"-0.5"} };
    private static final Object[] LARGE_NEGATIVE_ARG = { new String[]{"-20.5"} };
    private static final Object[] POSITIVE_ARG = { new String[]{"1"} };
    private static final Object[] DOUBLE_ARG = { new String[]{"3.5"} };
    private static final Object[] DOUBLE_ARG_MODULO = { new String[]{"42.5"} };

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, falls keine Argumente übergeben werden.")
    void testEmpty() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NO_ARGS));

        assertThat(result.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkte] Prüfe, ob für eine negative Zahl (-0.5) das korrekte Ergebnis (0.88) und sonst nichts ausgegeben wird.")
    void testNegativeInput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NEGATIVE_ARG));

        assertThat(asDouble(result)).isCloseTo(0.87758256189037271612, within(0.0001));
    }

    @Test
    @DisplayName("[1 Punkte] Prüfe, ob für eine betragsmäßig große, negative Zahl (-20.5) das korrekte Ergebnis (-0.08) und sonst nichts ausgegeben wird.")
    void testLargeNegativeInput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, LARGE_NEGATIVE_ARG));
        assertThat(asDouble(result)).isCloseTo(-0.07956356727854006, within(0.0001));
    }

    private double asDouble(Program.ExecutionResult<Void> result) {
        String firstLine = result.getOutput().lines().findFirst().orElse("empty output");
        try {
            return Double.parseDouble(firstLine);
        } catch (NumberFormatException ex) {
            assertThat(firstLine).withFailMessage("expected that »" + firstLine + "« can be parsed as double (did you output more text than requested?)").isEqualTo("a number");
            return 0;
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer gültigen Fließkommazahl (3.5) das korrekte Ergebnis (-0.93) und sonst nichts ausgegeben wird.")
    void testSmallPositiveInputExactOutput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, DOUBLE_ARG));

        assertThat(asDouble(result)).isCloseTo(-0.9364566872907963377, within(0.0001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer gültigen Fließkommazahl (3.5) die Ausgabe das korrekte Ergebnis (-0.93) enthält.")
    void testSmallPositiveInput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, DOUBLE_ARG));

        assertThat(result.getOutput()).contains("-0.9");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer gültigen Fließkommazahl außerhalb des vorgegebenen Intervalls (42.5) das korrekte Ergebnis (0.08) und sonst nichts ausgegeben wird.")
    void testLargePositiveInputExactOutput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, DOUBLE_ARG_MODULO));

        assertThat(asDouble(result)).isCloseTo(0.08838369930580555045, within(0.0001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer gültigen Fließkommazahl außerhalb des vorgegebenen Intervalls (42.5) die Ausgabe das korrekte Ergebnis (0.08) enthält.")
    void testLargePositiveInput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, DOUBLE_ARG_MODULO));
        assertThat(result.getOutput()).contains("0.08");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einem ganzzahligen Argument (1) das korrekte Ergebnis (0.5) und sonst nichts ausgegeben wird.")
    void testSmallIntegerInputExactOutput() {
        // außerdem gibt dieser Test Punkte, falls die Fakultät für zu große Zahlen (die gar nicht gefordert waren) falsch berechnet wird
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, POSITIVE_ARG));

        assertThat(asDouble(result)).isCloseTo(0.5403023058681397, within(0.0001));
    }
}
