import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class BallisticTest {

    private static final Object[] ZERO_ARG = {new String[]{"0", "0", "0"}};
    private static final Object[] X0_ARG = {new String[]{"2.5", "0", "0"}};
    private static final Object[] ALL_ARG = {new String[]{"10", "20.5", "2.75"}};

    private static final RuntimeClass BALLISTIC_CLASS = RuntimeClass.forName("Ballistic");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(BALLISTIC_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob für x0 = 0, v0 = 0 und t = 0 das Ergebnis '0' ausgegeben wird.")
    void testAllZero() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ZERO_ARG));;
        assertThat(result.getOutput().trim()).contains("0.0");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob für x0 = 2.5, v0 = 0 und t = 0 das Ergebnis '2.5' ausgegeben wird.")
    void testX0NotZero() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, X0_ARG));;
        assertThat(result.getOutput().trim()).contains("2.5");
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob für x0 = 10, v0 = 20.5 und t = 2.75 das Ergebnis '29.280937(4999)' ausgegeben wird.")
    void testAllNotZero() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ALL_ARG));;
        assertThat(result.getOutput().trim()).contains("29.280937");
    }
}
