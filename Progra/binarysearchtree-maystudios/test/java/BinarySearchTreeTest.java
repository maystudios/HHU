import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BinarySearchTreeTest {
    private static final RuntimeClass TREE_CLASS = RuntimeClass.forName("BinarySearchTree");

    private static final MethodCall<Integer> MAX_REC = MethodCall.<Integer>builder()
            .runtimeClass(TREE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("maximumRecursive")
            .parameterTypes(new Class<?>[]{ })
            .build();

    private static final MethodCall<Integer> MAX_IT = MethodCall.<Integer>builder()
            .runtimeClass(TREE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("maximumIterative")
            .parameterTypes(new Class<?>[]{ })
            .build();

    private static final MethodCall<Integer> HEIGHT = MethodCall.<Integer>builder()
            .runtimeClass(TREE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("height")
            .parameterTypes(new Class<?>[]{ })
            .build();

    private static final MethodCall<Integer> SUM = MethodCall.<Integer>builder()
            .runtimeClass(TREE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("sum")
            .parameterTypes(new Class<?>[]{ })
            .build();

    private static final MethodCall<String> REVERSE_ORDER = MethodCall.<String>builder()
            .runtimeClass(TREE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(String.class)
            .name("reverseOrder")
            .parameterTypes(new Class<?>[]{ })
            .build();

    private static final int[] MANY_ELEMENTS_TREE = {9, 7, 16, 5, 8, 12, 20, 22, 21};

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob maximumRecursive für einen leeren Baum die geforderte Exception wirft.")
    void testMaximumRecursiveEmptyTree() {
        var tree = new BinarySearchTree();
        assertThrows(java.util.NoSuchElementException.class, () -> MAX_REC.invoke(tree));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob maximumRecursive für einen Baum mit einem Element das Maximum findet.")
    void testMaximumRecursiveSmallTree() {
        var tree = new BinarySearchTree();
        tree.insert(-2);

        Program.ExecutionResult<Integer> result = Program.execute(() -> MAX_REC.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(-2);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob maximumRecursive für einen großen Baum (Einfügereihenfolge 9, 7, 16, 5, 8, 12, 20, 22, 21) das Maximum findet.")
    void testMaximumRecursiveBigTree() {
        var tree = new BinarySearchTree();
        for(int value: MANY_ELEMENTS_TREE) {
            tree.insert(value);
        }

        var rootBefore = getRoot(tree);

        Program.ExecutionResult<Integer> result = Program.execute(() -> MAX_REC.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(22);

        var rootAfter = getRoot(tree);

        Program.ExecutionResult<Integer> result2 = Program.execute(() -> MAX_REC.invoke(tree));
        assertThat(result2.getReturnValue().orElse(0xdeadbeef)).withFailMessage("mehrmalige Aufrufe von maximumRecursive liefern unterschiedliche Ergebnisse").isEqualTo(22);

        assertThat(rootBefore).withFailMessage("Wurzelelement hat sich durch Aufruf von maximumRecursive verändert").isEqualTo(rootAfter);
    }

    private Object getRoot(BinarySearchTree tree) {
        try {
            Field rootField = tree.getClass().getDeclaredField("root");
            rootField.setAccessible(true);
            return rootField.get(tree);
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob maximumIterative für einen leeren Baum die geforderte Exception wirft.")
    void testMaximumIterativeEmptyTree() {
        var tree = new BinarySearchTree();
        assertThrows(java.util.NoSuchElementException.class, () -> MAX_IT.invoke(tree));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob maximumIterative für einen Baum mit einem Element das Maximum findet.")
    void testMaximumIterativeSmallTree() {
        var tree = new BinarySearchTree();
        tree.insert(-2);

        Program.ExecutionResult<Integer> result = Program.execute(() -> MAX_IT.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(-2);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob maximumIterative für einen großen Baum (Einfügereihenfolge 9, 7, 16, 5, 8, 12, 20, 22, 21) das Maximum findet.")
    void testMaximumIterativeBigTree() {
        var tree = new BinarySearchTree();
        for(int value: MANY_ELEMENTS_TREE) {
            tree.insert(value);
        }

        var rootBefore = getRoot(tree);

        Program.ExecutionResult<Integer> result = Program.execute(() -> MAX_IT.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(22);

        var rootAfter = getRoot(tree);

        Program.ExecutionResult<Integer> result2 = Program.execute(() -> MAX_IT.invoke(tree));
        assertThat(result2.getReturnValue().orElse(0xdeadbeef)).withFailMessage("mehrmalige Aufrufe von maximumIterative liefern unterschiedliche Ergebnisse").isEqualTo(22);

        assertThat(rootBefore).withFailMessage("Wurzelelement hat sich durch Aufruf von maximumIterative verändert").isEqualTo(rootAfter);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob height für einen leeren Baum die Höhe 0 zurückgibt.")
    void testHeightEmptyTree() {
        var tree = new BinarySearchTree();
        Program.ExecutionResult<Integer> result = Program.execute(() -> HEIGHT.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(0);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob height für einen Baum mit einem Element 1 zurückgibt.")
    void testHeightSmallTree() {
        var tree = new BinarySearchTree();
        tree.insert(-2);

        Program.ExecutionResult<Integer> result = Program.execute(() -> HEIGHT.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob height für den großen Baum die Höhe 5 bestimmt.")
    void testHeightBigTree() {
        var tree = new BinarySearchTree();
        for(int value: MANY_ELEMENTS_TREE) {
            tree.insert(value);
        }

        Program.ExecutionResult<Integer> result = Program.execute(() -> HEIGHT.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(5);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob sum für einen leeren Baum 0 zurückgibt.")
    void testSumEmptyTree() {
        var tree = new BinarySearchTree();
        Program.ExecutionResult<Integer> result = Program.execute(() -> SUM.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(0);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob sum für einen Baum mit einem Element den richtigen Wert zurückgibt.")
    void testSumSmallTree() {
        var tree = new BinarySearchTree();
        tree.insert(-2);

        Program.ExecutionResult<Integer> result = Program.execute(() -> SUM.invoke(tree));
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(-2);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob sum für den großen Baum die richtige Summe bestimmt.")
    void testSumBigTree() {
        var tree = new BinarySearchTree();
        for(int value: MANY_ELEMENTS_TREE) {
            tree.insert(value);
        }

        Program.ExecutionResult<Integer> result = Program.execute(() -> SUM.invoke(tree));

        int sum = Arrays.stream(MANY_ELEMENTS_TREE).sum();
        assertThat(result.getReturnValue().orElse(0xdeadbeef)).isEqualTo(sum);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob reverseOrder für einen leeren Baum einen leeren String zurückgibt.")
    void testReverseOrderEmptyTree() {
        var tree = new BinarySearchTree();
        Program.ExecutionResult<String> result = Program.execute(() -> REVERSE_ORDER.invoke(tree));
        assertThat(result.getReturnValue().orElse("no output").trim()).isEqualTo("");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob reverseOrder für den großen Baum »22, 21, 20, 16, 12, 9, 8, 7, 5, « zurückgibt.")
    void testReverseOrderBigTree() {
        var tree = new BinarySearchTree();
        for(int value: MANY_ELEMENTS_TREE) {
            tree.insert(value);
        }

        Program.ExecutionResult<String> result = Program.execute(() -> REVERSE_ORDER.invoke(tree));
        assertThat(result.getReturnValue().orElse("no output").trim()).isEqualTo("22, 21, 20, 16, 12, 9, 8, 7, 5,");

        Program.ExecutionResult<String> result2 = Program.execute(() -> REVERSE_ORDER.invoke(tree));
        assertThat(result2.getReturnValue().orElse("no output").trim()).withFailMessage("mehrmalige Aufrufe von reverseOrder liefern unterschiedliche Ergebnisse").isEqualTo("22, 21, 20, 16, 12, 9, 8, 7, 5,");
    }

}
