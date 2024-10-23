import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class KniffelTest {

    private static final RuntimeClass KNIFFEL_CLASS = RuntimeClass.forName("Kniffel");

    private static final MethodCall<Integer> ACES_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("aces")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> TWOS_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("twos")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> THREES_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("threes")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> FOURS_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("fours")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> FIVES_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("fives")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> SIXES_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("sixes")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> THREE_OF_A_KIND_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("threeOfAKind")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> FOUR_OF_A_KIND_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("fourOfAKind")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> FULL_HOUSE_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("fullHouse")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> SMALL_STRAIGHT_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("smallStraight")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> LARGE_STRAIGHT_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("largeStraight")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> KNIFFEL_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("kniffel")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final MethodCall<Integer> CHANCE_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(KNIFFEL_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(int.class)
            .name("chance")
            .parameterTypes(new Class<?>[] { int[].class })
            .build();

    private static final Object[] THREE_OF_A_KIND_ARGS = { new int[]{ 1, 1, 1, 2, 3 } };
    private static final Object[] FOUR_OF_A_KIND_ARGS = { new int[]{ 1, 5, 5, 5, 5 } };
    private static final Object[] FULL_HOUSE_1 = { new int[]{ 1, 1, 1, 5, 5 } };
    private static final Object[] FULL_HOUSE_2 = { new int[]{ 1, 1, 5, 5, 5 } };
    private static final Object[] SMALL_STRAIGHT_ARGS = { new int[]{ 1, 2, 3, 3, 4 } };
    private static final Object[] SMALL_STRAIGHT_2_ARGS = { new int[]{ 1, 2, 3, 4, 6 } };
    private static final Object[] NO_SMALL_STRAIGHT_ARGS = { new int[]{ 1, 2, 3, 5, 6 } };
    private static final Object[] LARGE_STRAIGHT_1_ARGS = { new int[]{ 2, 3, 4, 5, 6 } };
    private static final Object[] LARGE_STRAIGHT_2_ARGS = { new int[]{ 1, 2, 3, 4, 5 } };
    private static final Object[] KNIFFEL_ARGS = { new int[]{ 2, 2, 2, 2, 2 } };

    private void assertPunktzahl(MethodCall<Integer> targetMethod, Object[] dice, int expected) {
        String diceString = IntStream.of((int[])dice[0])
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
        String category = targetMethod.getName();
        var result = Program.execute(() -> targetMethod.invoke(null, dice));
        Integer actual = result.getReturnValue().orElseThrow(() -> new AssertionError("%s: No return value for »%s«".formatted(category, diceString)));
        assertThat(actual)
                .withFailMessage("%s: Mit »%s« sollte man %d Punkte erhalten, das Programm hat aber %d Punkte berechnet.", category, diceString, expected, actual)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Einser korrekt berechnet werden. Eingaben für alle Tests sind:    1, 1, 1, 2, 3    2, 2, 2, 2, 1    1, 2, 2, 3, 4    1, 2, 3, 4, 6    2, 3, 4, 5, 6    2, 3, 4, 5, 6    2, 2, 2, 2, 2    1, 1, 1, 5, 5    1, 1, 5, 5, 5")
    void testOnes() {
        var targetMethod = ACES_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, 3);
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, 1);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, 1);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, 1);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, 1);
        assertPunktzahl(targetMethod, KNIFFEL_ARGS, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_1, 3);
        assertPunktzahl(targetMethod, FULL_HOUSE_2, 2);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Zweier bis Sechser korrekt berechnet werden.")
    void testNumbers() {
        assertPunktzahl(TWOS_METHOD, KNIFFEL_ARGS, 10);
        assertPunktzahl(THREES_METHOD, SMALL_STRAIGHT_ARGS, 6);
        assertPunktzahl(FOURS_METHOD, SMALL_STRAIGHT_2_ARGS, 4);
        assertPunktzahl(FIVES_METHOD, FOUR_OF_A_KIND_ARGS, 20);
        assertPunktzahl(SIXES_METHOD, LARGE_STRAIGHT_1_ARGS, 6);
        assertPunktzahl(SIXES_METHOD, THREE_OF_A_KIND_ARGS, 0);
    }

    @Test
    @DisplayName("[1 Punkte] Prüfe, ob Dreierpasch richtig bewertet wird.")
    void testThreeOfAKind() {
        var targetMethod = THREE_OF_A_KIND_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, 8);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_1, 13);
        assertPunktzahl(targetMethod, FULL_HOUSE_2, 17);
    }

    @Test
    @DisplayName("[1 Punkte] Prüfe, ob Viererpasch und Kniffel auch als Dreierpasch gewertet werden.")
    void testThreeOfAKindSubset() {
        var targetMethod = THREE_OF_A_KIND_METHOD;
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, 21);
        assertPunktzahl(targetMethod, KNIFFEL_ARGS, 10);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob Viererpasch richtig bewertet wird.")
    void testFourOfAKind() {
        var targetMethod = FOUR_OF_A_KIND_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, 21);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, KNIFFEL_ARGS, 10);
        assertPunktzahl(targetMethod, FULL_HOUSE_1, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_2, 0);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Full House grundsätzlich richtig bewertet wird.")
    void testFullHouse() {
        var targetMethod = FULL_HOUSE_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_1, 25);
        assertPunktzahl(targetMethod, FULL_HOUSE_2, 25);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, dass Kniffel nicht als Full House gewertet wird (keine Joker-Regel).")
    void testFullHouseKniffel() {
        assertPunktzahl(FULL_HOUSE_METHOD, KNIFFEL_ARGS, 0);
    }

    @Test
    @DisplayName("[1 Punkte] Prüfe, ob Kleine Straße richtig bewertet wird.")
    void testSmallStraight() {
        var targetMethod = SMALL_STRAIGHT_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, 30);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, 30);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, 30);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, 30);
        assertPunktzahl(targetMethod, KNIFFEL_ARGS, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_1, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_2, 0);
    }

    @Test
    @DisplayName("[1 Punkte] Prüfe, dass 1, 2, 3, 5, 6 nicht als Kleine Straße bewertet wird.")
    void testNoSmallStraight() {
        assertPunktzahl(SMALL_STRAIGHT_METHOD, NO_SMALL_STRAIGHT_ARGS, 0);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob Große Straße richtig bewertet wird.")
    void testLargeStraight() {
        var targetMethod = LARGE_STRAIGHT_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, 40);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, 40);
        assertPunktzahl(targetMethod, NO_SMALL_STRAIGHT_ARGS, 0);
        assertPunktzahl(targetMethod, KNIFFEL_ARGS, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_1, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_2, 0);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Kniffel richtig bewertet wird.")
    void testKniffel() {
        var targetMethod = KNIFFEL_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, 0);
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, 0);
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, 0);
        assertPunktzahl(targetMethod, KNIFFEL_ARGS, 50);
        assertPunktzahl(targetMethod, FULL_HOUSE_1, 0);
        assertPunktzahl(targetMethod, FULL_HOUSE_2, 0);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Chance richtig bewertet wird.")
    void testChance() {
        var targetMethod = CHANCE_METHOD;
        assertPunktzahl(targetMethod, THREE_OF_A_KIND_ARGS, IntStream.of((int[])THREE_OF_A_KIND_ARGS[0]).sum());
        assertPunktzahl(targetMethod, FOUR_OF_A_KIND_ARGS, IntStream.of((int[])FOUR_OF_A_KIND_ARGS[0]).sum());
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_ARGS, IntStream.of((int[])SMALL_STRAIGHT_ARGS[0]).sum());
        assertPunktzahl(targetMethod, SMALL_STRAIGHT_2_ARGS, IntStream.of((int[])SMALL_STRAIGHT_2_ARGS[0]).sum());
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_1_ARGS, IntStream.of((int[])LARGE_STRAIGHT_1_ARGS[0]).sum());
        assertPunktzahl(targetMethod, LARGE_STRAIGHT_2_ARGS, IntStream.of((int[])LARGE_STRAIGHT_2_ARGS[0]).sum());
        assertPunktzahl(targetMethod, KNIFFEL_ARGS, IntStream.of((int[])KNIFFEL_ARGS[0]).sum());
        assertPunktzahl(targetMethod, FULL_HOUSE_1, IntStream.of((int[])FULL_HOUSE_1[0]).sum());
        assertPunktzahl(targetMethod, FULL_HOUSE_2, IntStream.of((int[])FULL_HOUSE_2[0]).sum());
    }

}
