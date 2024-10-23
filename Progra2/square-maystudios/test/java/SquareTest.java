import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SquareTest {

    private static final RuntimeClass SQUARE_CLASS = RuntimeClass.forName("Square");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(SQUARE_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] ZERO_ARG = {new String[]{"0"}};
    private static final Object[] NEGATIVE_ARG = {new String[]{"-2"}};
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei Eingabe von '0' die Ausgabe '0' erfolgt.")
    void testZero() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ZERO_ARG), "");
        assertThat(result.getOutput().lines()).withFailMessage("expected »" + output + "« to contain exactly »0«, but it didn't (did you print a floating point number instead of an integer?)").containsExactly("0");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei Eingabe von '-2' die Ausgabe '4' erfolgt.")
    void testNegative() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NEGATIVE_ARG), "");
        assertThat(result.getOutput().lines()).containsExactly("4");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei Eingabe verschiedener Zahlen (1 bis 20) die richtigen Ausgaben als Ganzzahlen erfolgen.")
    void testVariousNumbersOutput() {
        for(int i = 1; i <= 20; i++) {
            final Object[] args = {new String[]{String.valueOf(i)}};
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, args));
            assertThat(result.getOutput().trim()).isEqualTo(String.valueOf(i * i));
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei Eingabe verschiedener Zahlen (1 bis 20) die richtigen Werte berechnet werden.")
    void testVariousNumbersValue() {
        for(int i = 1; i <= 20; i++) {
            final Object[] args = {new String[]{String.valueOf(i)}};
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, args));
            assertThat(result.getOutput().trim().replaceAll("\\.[0-9]*", "")).isEqualTo(String.valueOf(i * i));
        }
    }
}
