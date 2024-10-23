import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class PlateauBughuntTest {

    private static final RuntimeClass PLATEAU_CLASS = RuntimeClass.forName("Plateau");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
        .runtimeClass(PLATEAU_CLASS)
        .modifiers(Modifier.PUBLIC | Modifier.STATIC)
        .returnType(void.class)
        .name("main")
        .parameterTypes(new Class<?>[] { String[].class })
        .build();

    private static final Object[] EMPTY_ARGS = { new String[]{} };
    private static final Object[] NO_PLATEAU_ARGS = { new String[]{ "1", "2", "3", "4" } };
    private static final Object[] MINIMAL_PLATEAU_ARGS = { new String[]{ "1", "2", "1" } };
    private static final Object[] SINGLE_LONGEST_PLATEAU_ARGS = { new String[]{ "2", "3", "3", "1", "2", "2", "2", "2", "2", "1" } };
    private static final Object[] SINGLE_LONGEST_PLATEAU2_ARGS = { "0 1 1 2 2 2 1 1 0".split(" ") };
    private static final Object[] SINGLE_LONGEST_PLATEAU_FRONT_ARGS = { new String[]{ "2", "3", "3", "3", "1", "2", "2", "1", "1", "1" } };
    private static final Object[] MULTIPLE_LONGEST_PLATEAUS_ARGS = { new String[]{ "2", "3", "3", "3", "3", "1", "2", "2", "2", "2", "1", "4", "4", "4", "4", "3" } };
    private static final Object[] START_PLATEAU_ARGS = { new String[]{ "4", "4", "3", "3" } };
    private static final Object[] END_PLATEAU_ARGS = { new String[]{ "4", "5", "5", "5" } };
    private static final Object[] GEFAELLE_ARGS = { new String[]{ "1", "2", "1", "1", "1", "0" } };
    private static final Object[] GEFAELLE2_ARGS = { new String[]{ "1", "2", "1", "1", "1", "2", "1", "1", "1", "0" } };
    private static final Object[] START_SEEMS_LONGEST_PLATEAU = { new String[]{ "2", "2", "1", "3", "1", "3", "3", "3" } };

    private static final String EMPTY_INPUT = "";

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn das Programm ohne Parameter aufgerufen wird und eines der unnötigen Statements entfernt wurde.")
    void testEmptyArgs() throws Exception {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), EMPTY_INPUT);
        assertThat(result.getOutput()).contains("ERROR");

        System.setIn(null);
        try {
            MAIN_METHOD.invoke(null, EMPTY_ARGS);
        } catch(NullPointerException e) {
            fail("Wird ein Scanner benutzt?");
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm für die Eingabe '1 2 3' erkennt, dass kein Plateau existiert.")
    void testNoPlateau() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, NO_PLATEAU_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput().toLowerCase()).isEqualToIgnoringNewLines("kein plateau");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm für die Eingabe '1 2 1' die richtigen Werte ausgibt.")
    void testMinimalPlateau() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MINIMAL_PLATEAU_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput()).isEqualToIgnoringNewLines("1 1");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm für die Eingaben '2 3 3 1 2 2 2 2 2 1' und '0 1 1 2 2 2 1 1 0' die richtigen Werte ausgibt.")
    void testSingleLongestPlateau() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, SINGLE_LONGEST_PLATEAU_ARGS), EMPTY_INPUT);
        assertThat(result.getOutput()).isEqualToIgnoringNewLines("4 5");
        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, SINGLE_LONGEST_PLATEAU2_ARGS), EMPTY_INPUT);
        assertThat(result2.getOutput()).isEqualToIgnoringNewLines("3 3");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm für die Eingaben '2 3 3 3 1 2 2 1 1 1' und  '2 2 1 3 1 3 3 3' die richtigen Werte ausgibt.")
    void testSingleLongestFrontPlateau() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, SINGLE_LONGEST_PLATEAU_FRONT_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput()).isEqualToIgnoringNewLines("1 3");

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, START_SEEMS_LONGEST_PLATEAU), EMPTY_INPUT);

        assertThat(result2.getOutput()).isEqualToIgnoringNewLines("3 1");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm für die Eingabe '1 2 1 1 1 0' und '1 2 2 1 1 1 2 1 1 1 0' die richtigen Werte ausgibt.")
    void testGefaelleNichtAlsPlateauErkannt() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, GEFAELLE_ARGS), EMPTY_INPUT);
        assertThat(result.getOutput()).isEqualToIgnoringNewLines("1 1");

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, GEFAELLE2_ARGS), EMPTY_INPUT);
        assertThat(result2.getOutput()).isEqualToIgnoringNewLines("1 1");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm für die Eingabe '2 3 3 3 3 1 2 2 2 2 1 4 4 4 4 3' die richtigen Werte ausgibt.")
    void testMultipleLongestPlateau() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MULTIPLE_LONGEST_PLATEAUS_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput()).isEqualToIgnoringNewLines("1 4");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm für die Eingaben '4 4 3 3' und '4 5 5 5' kein Plateau ausgibt.")
    void testStartEndCannotBePlateau() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, START_PLATEAU_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput().toLowerCase()).isEqualToIgnoringNewLines("kein plateau");

        result = Program.execute(() -> MAIN_METHOD.invoke(null, END_PLATEAU_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput().toLowerCase()).isEqualToIgnoringNewLines("kein plateau");
    }
}
