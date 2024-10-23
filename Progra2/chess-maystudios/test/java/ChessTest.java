import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class ChessTest {

    private static final Object[] NO_ARGS = {new String[0]};
    private static final Object[] NEGATIVE_ARGS = {new String[]{"-1"}};
    private static final Object[] TEN_BY_TEN = {new String[]{"10"}};
    private static final Object[] FIVE_BY_FIVE = {new String[]{"5"}};
    private static final Object[] SIX_BY_SIX = {new String[]{"6"}};

    private static final RuntimeClass CHESS_CLASS = RuntimeClass.forName("Chess");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(CHESS_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn keine Argumente übergeben werden.")
    void testEmpty() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NO_ARGS));
        assertThat(result.getOutput()).startsWith("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn eine negative Zahl (-1) als Argument übergeben wird.")
    void testNegative() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NEGATIVE_ARGS));
        assertThat(result.getOutput()).startsWith("ERROR");
    }


    @Test
    @DisplayName("[2 Punkte] Prüfe, ob die Ausgabe 10 Zeilen lang und 10 Spalten breit ist, wenn als Argument die Zahl '10' übergeben wird.")
    void testFieldSize10() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, TEN_BY_TEN));
        assertThat(result.getOutput().lines())
                .hasSize(10)
                .allMatch(line -> line.length() == 10, "Die Ausgabe sollte 10 Zeichen pro Zeile haben. (auch Leerzeichen am Zeilenende nicht vergessen!)");
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob ein korrektes Schachbrett ausgegeben wird, wenn als Argument die Zahl '5' übergeben wird.")
    void testChessboard5() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, FIVE_BY_FIVE));
        assertThat(result.getOutput().lines()).containsExactly(
            "* * *",
            " * * ",
            "* * *",
            " * * ",
            "* * *"
        );
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob ein korrektes Schachbrett ausgegeben wird, wenn als Argument die Zahl '6' übergeben wird.")
    void testChessboard6() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, SIX_BY_SIX));
        assertThat(result.getOutput().lines()).containsExactly(
                "* * * ",
                " * * *",
                "* * * ",
                " * * *",
                "* * * ",
                " * * *"
        );
    }
}
