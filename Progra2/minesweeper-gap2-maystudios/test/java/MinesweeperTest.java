import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class MinesweeperTest {

    private static final RuntimeClass COSINUS_CLASS = RuntimeClass.forName("Minesweeper");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(COSINUS_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] ONE_ARG = { new String[]{"6"} };
    private static final Object[] DIM_WRONG_ARG = { new String[]{"6", "6", "1"} };
    private static final Object[] EXAMPLE_BIG = { new String[]{"6", "5", "1", "0", "0", "0", "1", "0", "1", "0", "0", "0", "1", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "0", "1", "1", "0", "1", "0"}};
    private static final Object[] ZERO_DIM = { new String[]{"0", "0"} };
    private static final Object[] EXAMPLE_SMALL = { new String[]{"2", "2", "0", "0", "1", "1"} };

    void testOneArg() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ONE_ARG));
        assertThat(result.getOutput()).contains("ERROR");
    }

    void testWrongDimensions() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, DIM_WRONG_ARG));
        assertThat(result.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Feld mit Dimension 0x0 funktioniert; außerdem Fehlermeldungen für fehlerhafte Argumente wie in Vorgabe weiterhin intakt")
    void test0x0() {
        // sicherstellen, dass übrige Programmlogik noch läuft
        testOneArg();
        testWrongDimensions();
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ZERO_DIM));
        assertThat(result.getOutput().toLowerCase()).contains("0 fields");
        assertThat(result.getOutput().toLowerCase()).contains("0 mines");
    }

    @Test
    @DisplayName("[1 Punkt] 2x2-Feld wird korrekt ausgegeben")
    void testSmallSquareBoard() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EXAMPLE_SMALL));
        assertThat(result.getOutput().lines().takeWhile(line -> line.length() <= 4).map(String::trim)).containsExactly("2 2", "x x");
    }

    @Test
    @DisplayName("[1 Punkt] 6x5-Feld wird korrekt ausgegeben")
    void testBigBoard() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EXAMPLE_BIG));
        assertThat(result.getOutput().lines().takeWhile(line -> line.length() <= 14).map(String::trim)).containsExactly("x 2   2 x 2", "x 3   2 x 2", "x 2   2 3 3", "2 3 2 3 x x", "1 x x 3 x 3");
    }

    @Test
    @DisplayName("[1 Punkt] Anzahl Minen von 6x5-Feld wird korrekt ausgegeben")
    void testBigBoardNumberOfMines() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EXAMPLE_BIG));
        assertThat(result.getOutput().toLowerCase()).contains("10 mines");
    }

    @Test
    @DisplayName("[1 Punkt] Anzahl Felder von 6x5-Feld wird korrekt ausgegeben")
    void testBigBoardNumberOfFields() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EXAMPLE_BIG));
        assertThat(result.getOutput().toLowerCase()).contains("30 fields");
    }

}
