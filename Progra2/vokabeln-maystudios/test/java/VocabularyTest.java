import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class VocabularyTest {

    private static final RuntimeClass VOCABULARY_CLASS = RuntimeClass.forName("Vocabulary");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
        .runtimeClass(VOCABULARY_CLASS)
        .modifiers(Modifier.PUBLIC | Modifier.STATIC)
        .returnType(void.class)
        .name("main")
        .parameterTypes(new Class<?>[] { String[].class })
        .build();

    private static final MethodCall<String> PERMUTIERT_METHOD = MethodCall.<String>builder()
            .runtimeClass(VOCABULARY_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(String.class)
            .name("permutiert")
            .parameterTypes(new Class<?>[] { String.class })
            .build();
	
    private static final Object[] EMPTY_ARGS = { new String[]{} };
	
    private static final String ONE_VOCAB = "snäll,lieb";
    private static final String ONE_VOCAB_SHORT = "där,da";
    private static final String ONE_VOCAB_SHORT2 = "nu,nun";
    private static final String MULTIPLE_VOCAB = "försvinna,verschwinden\nhalm,Stroh\nhej,hallo\nlivstid,Lebenszeit\ni,in\nredigera,bearbeiten\nstrå,Halm\nvisa,zeigen";

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer Vokabel eine richtige Ausgabe erfolgt.")
    void testOneLine() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), ONE_VOCAB);
        assertThat(result.getOutput().trim()).startsWith("snäll ");
        assertThat(result.getOutput().trim()).hasSize(10);
        // assertThat(result.getOutput().trim()).doesNotEndWith(" lieb"); // Ob sich die Permutation vom Orginalwort garantiert unterscheidet wird, wird an anderer Stelle schon getestet – dieser Test war damit nicht deterministisch
        assertThat(result.getOutput().trim().toCharArray()).contains('l', 'i', 'e', 'b');
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer Vokabel eine richtige Ausgabe erfolgt und diese tatsächlich zufällig ist, sich also bei vielen Aufrufen unterscheidet.")
    void testOneLineMultipleTimes() {
        Set<String> permutations = new HashSet<>();
        for(int repetition = 0; repetition < 100; repetition++) {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), ONE_VOCAB);
            assertThat(result.getOutput().trim()).startsWith("snäll ");
            assertThat(result.getOutput().trim()).hasSize(10);
            // assertThat(result.getOutput().trim()).doesNotEndWith(" lieb"); // Ob sich die Permutation vom Originalwort garantiert unterscheidet wird, wird an anderer Stelle schon getestet – dieser Test war damit nicht deterministisch
            assertThat(result.getOutput().trim().toCharArray()).contains('l', 'i', 'e', 'b');
            permutations.add(result.getOutput().trim());
        }
        assertThat(permutations).hasSizeGreaterThan(2);
    }

    private static boolean methodExists(MethodCall<?> methodCall) {
        Class clazz = methodCall.getRuntimeClass().getActual();
        try {
            clazz.getDeclaredMethod(methodCall.getName(), methodCall.getParameterTypes());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer Vokabel eine richtige Ausgabe erfolgt und diese tatsächlich zufällig ist, sich also bei vielen Aufrufen unterscheidet. (alternativ: Prüfe, ob permutiert(String) eine richtige, zufällige Permutation erzeugt.)")
    void testPermuteMultipleTimes() {
        if(methodExists(PERMUTIERT_METHOD) && !testWorks(this::testOneLineMultipleTimes)) {
            Set<String> permutations = new HashSet<>();
            for (int repetition = 0; repetition < 100; repetition++) {
                var result = Program.execute(() -> PERMUTIERT_METHOD.invoke(null, "lieb"), "").getReturnValue().orElse("empty").trim();
                assertThat(result).describedAs("permutiert-Methode getestet").hasSize(4);
                assertThat(result.toCharArray()).describedAs("permutiert-Methode getestet").contains('l', 'i', 'e', 'b');
                permutations.add(result);
            }
            assertThat(permutations).describedAs("permutiert-Methode getestet").hasSizeGreaterThan(2);
        } else {
            testOneLineMultipleTimes();
        }
    }

    private boolean testWorks(Runnable testMethod) {
        try {
            testMethod.run();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei kurzen Vokabeln eine richtige, zufällige Ausgabe erfolgt, die nicht dem Originalwort entspricht.")
    void testShortVocabs() {
        for(int repetition = 0; repetition < 20; repetition++) {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), ONE_VOCAB_SHORT);
            assertThat(result.getOutput().trim()).startsWith("där ");
            assertThat(result.getOutput().trim()).hasSize(6);
            assertThat(result.getOutput().trim()).doesNotEndWith(" da");
            assertThat(result.getOutput().trim().toCharArray()).contains('d', 'a');

            var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), ONE_VOCAB_SHORT2);
            assertThat(result2.getOutput().trim()).startsWith("nu ");
            assertThat(result2.getOutput().trim()).hasSize(6);
            assertThat(result2.getOutput().trim()).doesNotEndWith(" nun");
            assertThat(result2.getOutput().trim().toCharArray()).contains('n', 'u', 'n');
        }
    }

    @Test
    @DisplayName("[1 Punkte] Prüfe, ob bei mehreren Vokabeln die Ausgabe in der Form 'Vokabel Übersetzung' erfolgt.")
    void testMultipleLinesFormat() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), MULTIPLE_VOCAB);
        assertThat(result.getOutput().lines()).hasSize((int)MULTIPLE_VOCAB.lines().count());
        String[] split = result.getOutput().trim().split("\n");
        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            assertThat(line.trim()).hasSize(MULTIPLE_VOCAB.split("\n")[i].length());
            assertThat(line).contains(" ");
            assertThat(line.split(" ")[0]).isEqualTo(MULTIPLE_VOCAB.split("\n")[i].split(",")[0]);
        }

        var manyManyWords = String.join("\n", Collections.nCopies(54321, "i,in"));

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), manyManyWords);
        assertThat(result2.getOutput().lines()).hasSize((int)manyManyWords.lines().count());
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei mehreren Vokabeln die zufällige Anordnung der Buchstaben richtig erfolgt. (zufällige Anordnung soll nicht dem Original-Wort entsprechen)")
    void testMultipleLinesPermutation() {
        // mehrfach, weil Zufall
        for(int repetition = 0; repetition < 20; repetition++) {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), MULTIPLE_VOCAB);
            assertThat(result.getOutput().lines()).hasSize((int) MULTIPLE_VOCAB.lines().count());
            String[] split = result.getOutput().trim().split("\n");
            for (int i = 0; i < split.length; i++) {
                String line = split[i];
                assertThat(line.split(" ")[0]).isEqualTo(MULTIPLE_VOCAB.split("\n")[i].split(",")[0]);
                var sortedChars = line.split(" ")[1].toCharArray();
                Arrays.sort(sortedChars);
                var expectedSortedChars = MULTIPLE_VOCAB.split("\n")[i].split(",")[1].toCharArray();
                Arrays.sort(expectedSortedChars);
                assertThat(sortedChars).isEqualTo(expectedSortedChars);
                assertThat(line.split(" ")[1]).isNotEqualTo(MULTIPLE_VOCAB.split("\n")[i].split(",")[1]);
            }
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei mehreren Vokabeln die zufällige Anordnung der Buchstaben richtig erfolgt. (zufällige Anordnung soll nicht dem Original-Wort entsprechen) (alternativ: Prüfe, dass die permutiert-Methode nie das Original-Wort zurückgibt)")
    void testPermutiertNichtGleichOriginal() {
        if(methodExists(PERMUTIERT_METHOD) && !testWorks(this::testMultipleLinesPermutation)) {
            for (int repetition = 0; repetition < 100; repetition++) {
                var result = Program.execute(() -> PERMUTIERT_METHOD.invoke(null, "lieb"), "").getReturnValue().orElse("empty").trim();
                assertThat(result).describedAs("permutiert-Methode getestet").isNotEqualTo("lieb");
                assertThat(result).describedAs("permutiert-Methode getestet").hasSize(4);
                assertThat(result.toCharArray()).describedAs("permutiert-Methode getestet").contains('l', 'i', 'e', 'b');
            }
        } else {
            testMultipleLinesPermutation();
        }
    }

}
