import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class CosinusTest {

    private static final RuntimeClass COSINUS_CLASS = RuntimeClass.forName("Cosinus");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(COSINUS_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] PI_ARG = {new String[]{"3.14"}};
    private static final Object[] EMPTY_ARG = {new String[]{}};

    @Test
    @DisplayName("Teste, ob das Programm Cosinus kompiliert und sich wie vorgegeben verhält.")
    void testShipExists() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, PI_ARG), "");
        assertThat(result.getOutput().lines()).containsExactly("Wusstest du schon, dass der Cosinus von 3.14 gleich dem Cosinus von -3.14 ist?",
                "cos(3.14) = -0.9999987317275395",
                "cos(-3.14) = -0.9999987317275395");
        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARG), "");
        assertThat(result2.getOutput().lines().findFirst().orElse("")).startsWith("ERROR: Bitte eine Zahl als Argument übergeben");
        assertThat(result2.getOutput().lines()).hasSize(1);
    }
}
