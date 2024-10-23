import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class PascalTriangleTest {

    private static final RuntimeClass PASCALTRIANGLE_CLASS = RuntimeClass.forName("PascalTriangle");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
        .runtimeClass(PASCALTRIANGLE_CLASS)
        .modifiers(Modifier.PUBLIC | Modifier.STATIC)
        .returnType(void.class)
        .name("main")
        .parameterTypes(new Class<?>[] { String[].class })
        .build();

    private static final MethodCall<Integer> PASCAL_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(PASCALTRIANGLE_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("pascalRecursive")
            .parameterTypes(new Class<?>[] { int.class, int.class })
            .build();


    private static final Object[] ZERO_ARGS = { new String[]{ "0" } };
    private static final Object[] ONE_ARGS  = { new String[]{ "1" } };
    private static final Object[] TWO_ARGS  = { new String[]{ "2" } };
    private static final Object[] FIVE_ARGS = { new String[]{ "5" } };

    private static final String EMPTY_INPUT = "";

    // test for recursion values
    private static final Object[] TRIANGLE_1X1 = { 1, 1 };
    private static final Object[] TRIANGLE_6X6 = { 6, 6 };
    private static final Object[] TRIANGLE_5X2 = { 5, 2 };
    private static final Object[] TRIANGLE_15X7 = { 15, 7 };
    /**
     * Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 1, 1
     */
    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 1, 1")
    void testPascal1x1() {
        Program.ExecutionResult<Integer>  result = Program.execute(() -> PASCAL_METHOD.invoke(null, TRIANGLE_1X1));
        assertThat(result.getReturnValue()).get().isEqualTo(1);
    }
    /**
     * Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 6, 6
     */
    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 6, 6")
    void testPascal6x6() {
        Program.ExecutionResult<Integer>  result = Program.execute(() -> PASCAL_METHOD.invoke(null, TRIANGLE_6X6));
        assertThat(result.getReturnValue()).get().isEqualTo(1);
    }
    /**
     * Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 5, 2
     */
    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 5, 2")
    void testPascal5x2() {
        Program.ExecutionResult<Integer>  result = Program.execute(() -> PASCAL_METHOD.invoke(null, TRIANGLE_5X2));
        assertThat(result.getReturnValue()).get().isEqualTo(10);
    }
    /**
     * Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 15, 7
     */
    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Rekursionswert des Pascalschen Dreiecks stimmt an 15, 7")
    void testPascal15x7() {
        Program.ExecutionResult<Integer>  result = Program.execute(() -> PASCAL_METHOD.invoke(null, TRIANGLE_15X7));
        assertThat(result.getReturnValue()).get().isEqualTo(6435);
    }


    /**
     * Prüfe, ob Konsolen-Ausgabe des Pascalschen Dreiecks stimmt
     */
    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Konsolen-Ausgabe des Pascalschen Dreiecks stimmt")
    void testArgs0() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ZERO_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput().lines().map(String::trim)).containsExactly("1");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Konsolen-Ausgabe des Pascalschen Dreiecks stimmt")
    void testArgs1() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ONE_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput().lines().map(String::trim)).containsExactly("1", "1 1");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Konsolen-Ausgabe des Pascalschen Dreiecks stimmt")
    void testArgs2() {
        Locale.setDefault(Locale.ENGLISH);
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, TWO_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput().lines().map(String::trim)).containsExactly("1",
                "1 1",
                "1 2 1");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Konsolen-Ausgabe des Pascalschen Dreiecks stimmt")
    void testArgs5() {
        Locale.setDefault(Locale.ENGLISH);
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, FIVE_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput().lines().map(String::trim)).containsExactly("1",
                "1 1",
                "1 2 1",
                "1 3 3 1",
                "1 4 6 4 1",
                "1 5 10 10 5 1");
    }

}
