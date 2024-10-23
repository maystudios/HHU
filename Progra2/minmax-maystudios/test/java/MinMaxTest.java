import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class MinMaxTest {

    private static final Object[] NO_ARGS = {new String[0]};
    private static final Object[] SINGLE_ARG = {new String[]{"12"}};
    private static final Object[] MULTIPLE_ARGS_POSITIVE = {new String[]{"3", "7", "2", "121", "42", "25"}};
    private static final Object[] MULTIPLE_ARGS_MIXED = {new String[]{"24", "-9", "-52", "192", "49", "-25"}};
    private static final Object[] MULTIPLE_ARGS_MIXED_0 = new Object[]{ new String[]{"-12", "-23", "0"} };
    private static final Object[] MULTIPLE_ARGS_MIXED_N1 = new Object[]{ new String[]{"-1", "12", "23"} };
    private static final Object[] MULTIPLE_ARGS_MIXED_REPEATED_MIN = new Object[]{ new String[]{"2", "3", "2"} };
    private static final Object[] MULTIPLE_ARGS_MIXED_REPEATED_MAX = new Object[]{ new String[]{"3", "-4", "-5", "3"} };
    private static final Object[] MULTIPLE_ARGS_MAX_VALUE = {new String[]{String.valueOf(Integer.MAX_VALUE - 1), String.valueOf(Integer.MAX_VALUE)}};
    private static final Object[] MULTIPLE_ARGS_MIN_VALUE = {new String[]{String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MIN_VALUE + 1)}};

    private static final RuntimeClass MINMAX_CLASS = RuntimeClass.forName("MinMax");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(MINMAX_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn keine Argumente übergeben werden.")
    void testEmpty() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NO_ARGS));;
        assertThat(result.getOutput()).startsWith("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob für Minimum und Maximum jeweils '12' ausgegeben wird, wenn als Argument eine einzelne '12' übergeben wird.")
    void testSingle() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, SINGLE_ARG));;
        assertThat(result.getOutput().lines()).hasSize(2);
        assertThat(result.getOutput().lines()).withFailMessage("expected all lines to contain »12«").allMatch(line -> line.startsWith("12"));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das korrekte Ergebnis ausgegeben wird, wenn mehrere positive Zahlen als Argumente übergeben werden.")
    void testPositive() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_POSITIVE));;
        assertThat(result.getOutput().lines()).hasSize(2);
        assertThat(result.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(2, 121);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das korrekte Ergebnis ausgegeben wird, wenn die Zahlen sehr groß sind.")
    void testMaxInt() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_MAX_VALUE));;
        assertThat(result.getOutput().lines()).hasSize(2);
        assertThat(result.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(2147483646, 2147483647);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das korrekte Ergebnis ausgegeben wird, wenn die Zahlen sehr klein ist.")
    void testMinInt() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_MIN_VALUE));;
        assertThat(result.getOutput().lines()).hasSize(2);
        assertThat(result.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(-2147483648, -2147483647);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das korrekte Ergebnis ausgegeben wird, wenn mehrere positive und negative Zahlen als Argumente übergeben werden.")
    void testMixed() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_MIXED));;
        assertThat(result.getOutput().lines()).hasSize(2);
        assertThat(result.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(-52, 192);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Ausgabe eine ganze Zahl ist (keine Kommazahl)")
    void testIntegerOutput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, SINGLE_ARG));;
        assertThat(result.getOutput().lines()).hasSize(2);
        assertThat(result.getOutput().lines()).containsExactly("12", "12");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei '-1 12 23' und bei '-12 -23 0' die korrekten Ausgaben erfolgen")
    void testMagicNumbers() {
        var result0 = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_MIXED_0));
        assertThat(result0.getOutput().lines()).hasSize(2);
        assertThat(result0.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(-23, 0);

        var resultN1 = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_MIXED_N1));
        assertThat(resultN1.getOutput().lines()).hasSize(2);
        assertThat(resultN1.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(-1, 23);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei '2 3 2' und bei '3 -4 -5 3' die korrekten Ausgaben erfolgen")
    void testRepeatedNumbers() {
        var resultMin = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_MIXED_REPEATED_MIN));
        assertThat(resultMin.getOutput().lines()).hasSize(2);
        assertThat(resultMin.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(2, 3);

        var resultMax = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS_MIXED_REPEATED_MAX));
        assertThat(resultMax.getOutput().lines()).hasSize(2);
        assertThat(resultMax.getOutput().lines().mapToInt(MinMaxTest::safeParseInt)).containsExactly(-5, 3);
    }

    private static int safeParseInt(String s) {
        try {
            return (int)Double.parseDouble(s);
        } catch(NumberFormatException e) {
            fail("»" + s + "« cannot be parsed as number; did you output more text than requested?");
        }
        return -1337;
    }
}
