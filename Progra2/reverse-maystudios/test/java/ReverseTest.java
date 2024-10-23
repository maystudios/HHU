import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class ReverseTest {

    private static final RuntimeClass REVERSE_CLASS = RuntimeClass.forName("Reverse");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(REVERSE_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] NO_ARGS = { new String[0] };
    private static final Object[] SINGLE_ARG = { new String[]{"Test"} };
    private static final Object[] MULTIPLE_ARGS = { new String[]{"1", "2", "3", "4", "5"} };

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob nichts ausgegeben wird, wenn keine Argumente übergeben werden.")
    void testEmpty() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NO_ARGS), "");
        assertThat(result.getOutput()).isEmpty();
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob die korrekte Ausgabe erfolgt, wenn genau ein Argument übergeben wird.")
    void testSingle() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, SINGLE_ARG), "");
        assertThat(result.getOutput().lines()).containsExactly("Test");
    }

    @Test
    @DisplayName("[3 Punkte] Prüfe, ob die korrekte Ausgabe erfolgt, wenn mehrere Argumente übergeben werden.")
    void testMultiple() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_ARGS), "");
        assertThat(result.getOutput().lines()).containsExactly("5", "4", "3", "2", "1");
    }
}
