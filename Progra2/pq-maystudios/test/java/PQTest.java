import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class PQTest {

    private static final Object[] NO_ARGS = {new String[0]};
    private static final Object[] ONE_ARG = {new String[]{"12"}};
    private static final Object[] THREE_ARGS = {new String[]{"12", "1", "1"}};
    private static final Object[] ARGS_0_1P4 = {new String[]{"0", "1.4"}};
    private static final Object[] ARGS_M1_0 = {new String[]{"1", "0"}};
    private static final Object[] ARGS_2_M0P5 = {new String[]{"2", "-0.5"}};
    private static final Object[] ARGS_16_64 = {new String[]{"16", "64"}};
    private static final Object[] ARGS_16_064 = {new String[]{"1.6", "0.64"}};
    private static final Object[] ARGS_02_001 = {new String[]{"-0.2", "0.01"}};
    private static final Object[] ARGS_53_702 = {new String[]{"-5.3", "7.02"}};

    private static final RuntimeClass PQ_CLASS = RuntimeClass.forName("PQ");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(PQ_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn keine Argumente übergeben werden, und das Programm danach nicht abstürzt.")
    void testNoArgument() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NO_ARGS));
        assertThat(result.getOutput().lines().findFirst().orElse("no output")).startsWith("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn ein Argument übergeben werden, und das Programm danach nicht abstürzt.")
    void testOneArgument() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ONE_ARG));
        assertThat(result.getOutput().lines().findFirst().orElse("no output")).startsWith("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn drei Argumente übergeben werden.")
    void testThreeArguments() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, THREE_ARGS));
        assertThat(result.getOutput().lines().findFirst().orElse("no output")).startsWith("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, dass für x² + 1.4 keine Nullstelle ausgegeben wird.")
    void testNoRoots() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_0_1P4));
        assertThat(result.getOutput()).contains("0 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("1 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("2 Nullstelle");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die 2 Nullstellen von x² + x korrekt berechnet werden.")
    void testTwoRoots() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_M1_0));
        assertThat(asDouble(result.getOutput().lines().findFirst().orElse("no output"))).isCloseTo(-1, within(0.0001));
        assertThat(asDouble(result.getOutput().lines().skip(1).findFirst().orElse("no output"))).isCloseTo(0, within(0.0001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei den 2 Nullstellen von x² + x auch der Text ausgegeben wird, dass es zwei Nullstellen gibt.")
    void testTwoRootsNumberOfRoot() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_M1_0));
        assertThat(result.getOutput()).contains("2 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("1 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("0 Nullstelle");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die 2 Nullstellen von x² + 2x - 0.5 korrekt berechnet werden und die Anzahl der Nullstellen ausgegeben wird.")
    void testTwoFractionalRoots() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_2_M0P5));
        assertThat(asDouble(result.getOutput().lines().findFirst().orElse("no output"))).isCloseTo(-2.224744871391589, within(0.0001));
        assertThat(asDouble(result.getOutput().lines().skip(1).findFirst().orElse("no output"))).isCloseTo(0.22474487, within(0.0001));
        assertThat(result.getOutput()).contains("2 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("1 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("0 Nullstelle");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die 1 Nullstelle von x² + 16x + 64, x² + 1.6 x + 0.64 und x² - 0.2x + 0.01 und die zwei Nullstellen von x² - 5.3x + 7.02 korrekt berechnet werden und die Anzahl der Nullstellen ausgegeben wird.")
    void testOneRoots() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_16_64));
        assertThat(result.getOutput()).withFailMessage("x² + 16x + 64 hat 1 Nullstelle, die Ausgabe ist aber:\n%s", result.getOutput()).contains("1 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("2 Nullstelle");
        assertThat(result.getOutput()).doesNotContain("0 Nullstelle");
        assertThat(asDouble(result.getOutput().lines().findFirst().orElse("no output"))).isCloseTo(-8, within(0.0001));

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_16_064));
        assertThat(result2.getOutput()).withFailMessage("x² + 1.6 x + 0.64 hat 1 Nullstelle, die Ausgabe ist aber:\n%s", result2.getOutput()).contains("1 Nullstelle");
        assertThat(result2.getOutput()).doesNotContain("2 Nullstelle");
        assertThat(result2.getOutput()).doesNotContain("0 Nullstelle");
        assertThat(asDouble(result2.getOutput().lines().findFirst().orElse("no output"))).isCloseTo(-0.8, within(0.0001));

        // 0.01 ist nicht exakt in double precision darstellbar, spätestens hier sollte also ein `== 0`-Check fehlschlagen
        var result3 = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_02_001));
        assertThat(result3.getOutput()).withFailMessage("x² - 0.2x + 0.01 hat 1 Nullstelle, die Ausgabe ist aber:\n%s", result3.getOutput()).contains("1 Nullstelle");
        assertThat(result3.getOutput()).doesNotContain("2 Nullstelle");
        assertThat(result3.getOutput()).doesNotContain("0 Nullstelle");
        assertThat(asDouble(result3.getOutput().lines().findFirst().orElse("no output"))).isCloseTo(0.1, within(0.0001));

        // Manche prüfen Math.round(x1) == Math.round(x2)
        var result4 = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_53_702));
        assertThat(result4.getOutput()).withFailMessage("x² - 5.3x + 7.02 hat 2 Nullstellen, die Ausgabe ist aber:\n%s", result4.getOutput()).contains("2 Nullstelle");
        assertThat(result4.getOutput()).doesNotContain("1 Nullstelle");
        assertThat(result4.getOutput()).doesNotContain("0 Nullstelle");
        assertThat(asDouble(result4.getOutput().lines().findFirst().orElse("no output"))).isCloseTo(2.6, within(0.0001));
        assertThat(asDouble(result4.getOutput().lines().skip(1).findFirst().orElse("no output"))).isCloseTo(2.7, within(0.0001));
    }

    private double asDouble(String line) {
        try {
            return Double.parseDouble(line);
        } catch(NumberFormatException ex) {
            assertThat(line).withFailMessage("expected that »" + line + "« can be parsed as double; please stick to the output format given in the task").isEqualTo("a number");
            return 1337;
        }
    }

}
