import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest {

    private static final RuntimeClass SEARCH_CLASS = RuntimeClass.forName("Search");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(SEARCH_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[]{String[].class})
            .build();

    private static final MethodCall<Integer> SPLIT_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(SEARCH_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("split")
            .parameterTypes(new Class<?>[]{int[].class, int.class, int.class, int.class})
            .build();

    private static final MethodCall<Integer> SEARCH_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(SEARCH_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("search")
            .parameterTypes(new Class<?>[]{int[].class, int.class})
            .build();

    private static final Object[] MAIN_ARGS = { new String[] {"7", "2", "4", "7", "9", "12", "21", "26", "31", "37"} };
    private static final Object[] MAIN_ARGS_NOT_FOUND = { new String[] {"17", "2", "4", "7", "9", "12", "21", "26", "31", "37"} };

    private static final int[] EMPTY_ARRAY = new int[]{};
    private static final int[] SINGLETON_ARRAY = new int[]{-3};
    private static final int[] REPETITION_ARRAY = new int[]{5, 5};
    private static final int[] REPETITION_ARRAY2 = new int[]{2, 5, 5};
    private static final int[] SEARCH_ARRAY = new int[]{2, 4, 7, 9, 12, 21, 26, 31, 37};
    private static final int[] SEARCH_ARRAY_VARIANCE = new int[]{-81, 2, 3, 4};

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode split() korrekt für das Array {2, 4, 7, 9, 12, 21, 26, 31, 37} funktioniert.")
    void testSplit() {
        assertSplitCorrect(SEARCH_ARRAY, 7, 0, 8, 1);
        assertSplitCorrect(SEARCH_ARRAY, 21, 4, 8, 5);
        assertSplitCorrect(SEARCH_ARRAY, 21, 0, 8, 4);
    }

    private void assertSplitCorrect(int[] array, int needle, int left, int right, int expected) {
        var result1 = Program.execute(() -> SPLIT_METHOD.invoke(null, array, needle, left, right));
        assertThat(result1.getReturnValue())
                .withFailMessage("Bei der Suche nach %d mit l=%d und r=%d sollte die Splitposition %d sein, die Methode hat aber %d berechnet.", needle, left, right, expected, result1.getReturnValue().get())
                .contains(expected);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode split() korrekt für das Array {2, 5, 5} funktioniert für l=1, r=2.")
    void testSplitRepetition() {
        assertSplitCorrect(REPETITION_ARRAY2, 21, 1, 2, 1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode search() bei einem leeren Array den Wert -1 zurückgibt.")
    void testSearchEmpty() {
        assertSearchWorks(EMPTY_ARRAY, 0, -1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode search() bei einem einelementigen Array und erfolgloser/erfolgreicher Suche funktioniert.")
    void testSingleton() {
        assertSearchWorks(SINGLETON_ARRAY, 0, -1);
        assertSearchWorks(SINGLETON_ARRAY, -3, 0);
    }

    private void assertSearchWorks(int[] array, int needle, int index) {
        var result = Program.execute(() -> SEARCH_METHOD.invoke(null, array, needle));
        assertThat(result.getReturnValue())
                .withFailMessage("Bei der Suche nach %d in %s sollte die Position %d sein, die Methode hat aber %d berechnet.", needle, Arrays.toString(array), index, result.getReturnValue().get())
                .contains(index);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode search() im Array {5, 5} erfolgreich/erfolglos suchen kann.")
    void testRepetition() {
        assertSearchWorks(REPETITION_ARRAY, 0, -1);
        var result2 = Program.execute(() -> SEARCH_METHOD.invoke(null, REPETITION_ARRAY, 5));
        assertThat(result2.getReturnValue().toString()).containsPattern(Pattern.compile("[01]"));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode search() korrekt Werte in {2, 4, 7, 9, 12, 21, 26, 31, 37} findet.")
    void testSearch() {
        var result1 = Program.execute(() -> SEARCH_METHOD.invoke(null, SEARCH_ARRAY, 2));
        var result2 = Program.execute(() -> SEARCH_METHOD.invoke(null, SEARCH_ARRAY, 12));
        var result3 = Program.execute(() -> SEARCH_METHOD.invoke(null, SEARCH_ARRAY, 37));

        assertThat(result1.getReturnValue()).contains(0);
        assertThat(result2.getReturnValue()).contains(4);
        assertThat(result3.getReturnValue()).contains(8);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode search() korrekt Werte in {-81, 2, 3, 4} (nicht) findet.")
    void testSearchGreaterVariance() {
        for (int i = 0; i < SEARCH_ARRAY_VARIANCE.length; i++) {
            int needle = SEARCH_ARRAY_VARIANCE[i];
            assertSearchWorks(SEARCH_ARRAY_VARIANCE, needle, i);
        }
        for (int element: SEARCH_ARRAY_VARIANCE) {
            int needle = element + 3;
            assertSearchWorks(SEARCH_ARRAY_VARIANCE, needle, -1);
            int needle2 = element - 3;
            assertSearchWorks(SEARCH_ARRAY_VARIANCE, needle2, -1);
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die main-Methode richtig implementiert wurde (erfolgreiche Suche).")
    void testMain() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MAIN_ARGS));

        assertThat(result.getOutput()).isEqualToIgnoringNewLines("2");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die main-Methode richtig implementiert wurde (erfolglose Suche, Argumente 17 2 4 7 9 12 21 26 31 37).")
    void testMainNotFound() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MAIN_ARGS_NOT_FOUND));

        assertThat(result.getOutput()).isEqualToIgnoringNewLines("-1");
    }
}
