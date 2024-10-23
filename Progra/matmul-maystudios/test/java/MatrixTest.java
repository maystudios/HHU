import de.hhu.educode.testing.ConstructorCall;
import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class MatrixTest {

    private static final RuntimeClass MATRIX_CLASS = RuntimeClass.forName("Matrix");

    private static final ConstructorCall CONSTRUCTOR = ConstructorCall.builder()
            .runtimeClass(MATRIX_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{int[][].class})
            .build();

    private static final MethodCall<Matrix> MULTIPLY = MethodCall.<Matrix>builder()
        .runtimeClass(MATRIX_CLASS)
        .modifiers(Modifier.PUBLIC)
        .returnType(Matrix.class)
        .name("multiply")
        .parameterTypes(new Class<?>[] { Matrix.class })
        .build();

    private static final MethodCall<Integer> GET = MethodCall.<Integer>builder()
            .runtimeClass(MATRIX_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("get")
            .parameterTypes(new Class<?>[] { int.class, int.class })
            .build();

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Erstellen einer Matrix funktioniert")
    void testConstructor() {
        var mat1 = new int[][]{ new int[]{ 2, 8, 9 }, new int[]{ 1, 12, 15 }, new int[]{ 42, 3, 6 }, new int[]{ 50, 21, 4 } };
        var result = Program.execute(() -> CONSTRUCTOR.invoke((Object) mat1));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob get korrekt funktioniert (wird in anderen Tests benötigt)")
    void testGet() {
        var mat1 = new int[][]{ new int[]{ 2, 8, 9 }, new int[]{ 1, 12, 15 }, new int[]{ 42, 3, 6 }, new int[]{ 50, 21, 4 } };
        var mat = CONSTRUCTOR.invoke((Object) mat1);
        assertThat(GET.invoke(mat, 0, 0)).isEqualTo(2);
        assertThat(GET.invoke(mat, 0, 1)).isEqualTo(8);
        assertThat(GET.invoke(mat, 1, 0)).isEqualTo(1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob get eine IndexOutOfBoundsException (aber keine ArrayIndexOutOfBoundsException) wirft, wenn ein Index ungültig ist")
    void testGetException() {
        var mat1 = new int[][]{ new int[]{ 2, 8, 9 }, new int[]{ 1, 12, 15 }, new int[]{ 42, 3, 6 }, new int[]{ 50, 21, 4 } };
        var mat = CONSTRUCTOR.invoke((Object) mat1);
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GET.invoke(mat, 0, -1));
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GET.invoke(mat, -1, 0));
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GET.invoke(mat, 4, 0));
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GET.invoke(mat, 5, 0));
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GET.invoke(mat, 0, 3));
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GET.invoke(mat, 0, 4));
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> GET.invoke(mat, 10, 10));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Erstellen einer ungültigen Matrix eine IllegalArgumentException wirft")
    void testConstructorInvalid() {
        var mat1 = new int[][]{ new int[]{ 2, 8, 9 }, new int[]{ 1, 12, 15 }, new int[]{ 42, 3}, new int[]{ 50, 21, 4 } };
        assertThrows(IllegalArgumentException.class, () -> CONSTRUCTOR.invoke((Object) mat1));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Erstellen einer Matrix mit null eine IllegalArgumentException wirft")
    void testConstructorNull() {
        assertThrows(IllegalArgumentException.class, () -> CONSTRUCTOR.invoke((Matrix) null));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Erstellen einer Matrix mit null-Zeile eine IllegalArgumentException wirft")
    void testRowNull() {
        var mat1 = new int[][]{ new int[]{ 1, 2, 7 }, null };
        assertThrows(IllegalArgumentException.class, () -> CONSTRUCTOR.invoke((Object)mat1));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Multiplikation zweier quadratischer Matrizen funktioniert")
    void testSquareMatrices() {
        var mat1 = new int[][]{ new int[]{ 1, 2 }, new int[]{ 2, 6 } };
        var mat2 = new int[][]{ new int[]{ 6, 8 }, new int[]{ 5, 12 } };
        var result = Program.execute(() -> MULTIPLY.invoke(CONSTRUCTOR.invoke((Object) mat1), CONSTRUCTOR.invoke((Object) mat2))).getReturnValue().get();

        assertThat(GET.invoke(result, 0, 0)).isEqualTo(16);
        assertThat(GET.invoke(result, 0, 1)).isEqualTo(32);
        assertThat(GET.invoke(result, 1, 0)).isEqualTo(42);
        assertThat(GET.invoke(result, 1, 1)).isEqualTo(88);
        assertThrows(Exception.class, () -> GET.invoke(result, 1, 2));
        assertThrows(Exception.class, () -> GET.invoke(result, 2, 0));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Multiplikation zweier kompatibler Matrizen mit unterschiedlichen Dimensionen funktioniert")
    void testMatMul() {
        var mat1 = new int[][]{ new int[]{ 1, 2, 7, 8 }, new int[]{ 2, 6, 5, 3 } };
        var mat2 = new int[][]{ new int[]{ 2, 8, 9 }, new int[]{ 1, 12, 15 }, new int[]{ 42, 3, 6 }, new int[]{ 50, 21, 4 } };
        var result = Program.execute(() -> MULTIPLY.invoke(CONSTRUCTOR.invoke((Object) mat1), CONSTRUCTOR.invoke((Object) mat2))).getReturnValue().get();

        assertThat(GET.invoke(result, 0, 0)).isEqualTo(698);
        assertThat(GET.invoke(result, 0, 1)).isEqualTo(221);
        assertThat(GET.invoke(result, 0, 2)).isEqualTo(113);
        assertThat(GET.invoke(result, 1, 0)).isEqualTo(370);
        assertThat(GET.invoke(result, 1, 1)).isEqualTo(166);
        assertThat(GET.invoke(result, 1, 2)).isEqualTo(150);
        assertThrows(Exception.class, () -> GET.invoke(result, 1, 3));
        assertThrows(Exception.class, () -> GET.invoke(result, 2, 0));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob multiply eine IllegalArgumentException wirft, wenn die Matrixdimensionen nicht kompatibel sind")
    void testDimensionMismatch() {
        var mat1 = CONSTRUCTOR.invoke((Object) new int[][]{ new int[]{ 1, 2, 7 }, new int[]{ 2, 6, 5 } });
        var mat2 = CONSTRUCTOR.invoke((Object) new int[][]{ new int[]{ 2, 8, 9 }, new int[]{ 1, 12, 15 }, new int[]{ 42, 3, 6 }, new int[]{ 50, 21, 4 } });

        assertThrows(IllegalArgumentException.class, () -> MULTIPLY.invoke(mat1, mat2));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine IllegalArgumentException geworfen wird, wenn mit null multipliziert werden soll")
    void testNull() {
        var mat1 = CONSTRUCTOR.invoke((Object) new int[][]{ new int[]{ 1, 2, 7 }, new int[]{ 2, 5, 7 } });
        assertThrows(IllegalArgumentException.class, () -> MULTIPLY.invoke(mat1, (Object)null));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Matrix keine nicht-privaten Felder hat")
    void testFieldsPrivate() {
        assertThat(MATRIX_CLASS.getActual().getDeclaredFields()).allMatch(field -> (field.getModifiers() | Modifier.PRIVATE) == field.getModifiers());
    }
}
