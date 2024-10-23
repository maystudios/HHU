import de.hhu.educode.testing.ConstructorCall;
import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest {

    private static final RuntimeClass LIST_CLASS = RuntimeClass.forName("List");

    private static final ConstructorCall DEFAULT_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final ConstructorCall CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{int[].class})
            .build();

    private static final MethodCall<Void> ADD_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("add")
            .parameterTypes(new Class<?>[]{int.class})
            .build();

    private static final MethodCall<Void> ADD_ALL_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("addAll")
            .parameterTypes(new Class<?>[]{int[].class})
            .build();

    private static final MethodCall<Integer> GET_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("get")
            .parameterTypes(new Class<?>[]{int.class})
            .build();

    private static final MethodCall<Integer> LENGTH_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("length")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<String> TO_STRING_METHOD = MethodCall.<String>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(String.class)
            .name("toString")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Integer> FIND_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("find")
            .parameterTypes(new Class<?>[]{int.class})
            .build();

    private static final MethodCall<Integer> FIND_LAST_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("findLast")
            .parameterTypes(new Class<?>[]{int.class})
            .build();

    private static final MethodCall<Void> REMOVE_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("remove")
            .parameterTypes(new Class<?>[]{int.class})
            .build();

    private static final MethodCall<Void> REMOVE_FIRST_OCCURENCE_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("removeFirstOccurrence")
            .parameterTypes(new Class<?>[]{int.class})
            .build();

    private static final MethodCall<Void> REMOVE_LAST_OCCURENCE_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("removeLastOccurrence")
            .parameterTypes(new Class<?>[]{int.class})
            .build();

    private static final MethodCall<Void> SORT_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("sort")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final Object ADD_ALL_ARRAY = new int[]{5, 6, 7, 8, 9};
    private static final Object FIND_ARRAY = new int[]{5, 5, 7, 8, 8};
    private static final Object REMOVE_OCCURRENCE_ARRAY = new int[]{5, 6, 5, 7, 5, 8};
    private static final Object SORT_ARRAY = new int[]{3, 1, 4, 7, 2, 8, 1, 2};

    private List newList(Object elements) {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        for(int element: (int[])elements) {
            ADD_METHOD.invoke(list, element);
        }
        return (List)list;
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Hinzufügen eines neuen Elements (5) in eine leere Liste funktioniert.")
    void testAddNodeToEmptyList() {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        ADD_METHOD.invoke(list, 5);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Hinzufügen mehrerer neuer Elemente (5,6,7,8,9) mit add funktioniert.")
    void testAddNode() {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        ADD_METHOD.invoke(list, 5);
        ADD_METHOD.invoke(list, 6);
        ADD_METHOD.invoke(list, 7);
        ADD_METHOD.invoke(list, 8);
        ADD_METHOD.invoke(list, 9);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(8);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(9);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob get mit zu großem Index das letzte Element zurückgibt.")
    void testGet() {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        ADD_METHOD.invoke(list, 5);
        ADD_METHOD.invoke(list, 6);

        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(6);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob die Methode addAll() für die Elemente (5,6,7,8,9) korrekt funktioniert,")
    void testAddAll() {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        ADD_ALL_METHOD.invoke(list, ADD_ALL_ARRAY);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(8);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(9);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob der Konstruktor für die Elemente (5,6,7,8,9) korrekt funktioniert.")
    void testConstructor() {
        var list = CONSTRUCTOR.invoke(ADD_ALL_ARRAY);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(8);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(9);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode length() für die Liste (5,6,7,8,9) 5 zurückgibt.")
    void testLength() {
        var list = newList(ADD_ALL_ARRAY);
        var result = Program.execute(() -> LENGTH_METHOD.invoke(list));
        // second call to assure that list is not broken
        var result2 = Program.execute(() -> LENGTH_METHOD.invoke(list));

        assertThat(result.getReturnValue().orElse(-1)).isEqualTo(5);
        assertThat(result2.getReturnValue().orElse(-1)).isEqualTo(5);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode length() für die leere Liste 0 zurückgibt.")
    void testLengthEmptyList() {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        var result = Program.execute(() -> LENGTH_METHOD.invoke(list));

        assertThat(result.getReturnValue().orElse(-1)).isZero();
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode toString() für die Liste (5,6,7,8,9) die Zahlen in korrekter Reihenfolge ausgibt, und die Rückgabe für eine leere Liste leer ist.")
    void testToStringOrder() {
        var list = newList(ADD_ALL_ARRAY);
        var result = Program.execute(() -> TO_STRING_METHOD.invoke(list));

        assertThat(result.getReturnValue().orElse("")).contains("5,6,7,8,9");

        var list2 = newList(new int[]{});
        var result2 = Program.execute(() -> TO_STRING_METHOD.invoke(list2));
        assertThat(result2.getReturnValue().orElse("/")).isEmpty();
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode toString() für die Liste (5,6,7,8,9) korrekt funktioniert, d. h. nicht mit einem Komma endet.")
    void testToString() {
        var list = newList(ADD_ALL_ARRAY);
        var result = Program.execute(() -> TO_STRING_METHOD.invoke(list));

        assertThat(result.getReturnValue()).contains("5,6,7,8,9");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode find() für die Liste (5,5,7,8,8) korrekt funktioniert.")
    void testFind() {
        var list = newList(FIND_ARRAY);

        assertThat(FIND_METHOD.invoke(list, 5)).isEqualTo(0);
        assertThat(FIND_METHOD.invoke(list, 7)).isEqualTo(2);
        assertThat(FIND_METHOD.invoke(list, 8)).isEqualTo(3);
        assertThat(FIND_METHOD.invoke(list, 9)).isEqualTo(-1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode findLast() für die Liste (5,5,7,8,8) korrekt funktioniert.")
    void testFindLast() {
        var list = newList(FIND_ARRAY);

        assertThat(FIND_LAST_METHOD.invoke(list, 5)).isEqualTo(1);
        assertThat(FIND_LAST_METHOD.invoke(list, 7)).isEqualTo(2);
        assertThat(FIND_LAST_METHOD.invoke(list, 8)).isEqualTo(4);
        assertThat(FIND_LAST_METHOD.invoke(list, 9)).isEqualTo(-1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode remove() für die Liste (5,6,7,8,9) korrekt funktioniert, wenn nur der Kopf entfernt werden soll.")
    void testRemoveHead() {
        var list = newList(ADD_ALL_ARRAY);
        REMOVE_METHOD.invoke(list, 0);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(8);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(9);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(9);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode remove() für die Liste (5,6,7,8,9) korrekt funktioniert, wenn nur das letzte Element entfernt wird.")
    void testRemoveLast() {
        var list = newList(ADD_ALL_ARRAY);
        REMOVE_METHOD.invoke(list, 4);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(8);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(8);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode remove() für die Liste (5,6,7,8,9) korrekt funktioniert, wenn mehrere Elemente nacheinander entfernt werden sollen.")
    void testRemove() {
        var list = newList(ADD_ALL_ARRAY);
        REMOVE_METHOD.invoke(list, 1);
        REMOVE_METHOD.invoke(list, 3);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(8);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(8);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode remove() für die Listen (5,6,7,8,9) und () [leere Liste] zu große/kleine Indizes ignoriert.")
    void testRemoveInvalid() {
        var list = newList(ADD_ALL_ARRAY);
        REMOVE_METHOD.invoke(list, -1);
        REMOVE_METHOD.invoke(list, 5);
        
        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(8);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(9);
        assertThat(GET_METHOD.invoke(list, 5)).isEqualTo(9);

        var list2 = DEFAULT_CONSTRUCTOR.invoke();
        // this should not crash
        REMOVE_METHOD.invoke(list, -1);
        REMOVE_METHOD.invoke(list, 1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode removeFirstOccurrence() für die Liste (5,6,5,7,5,8) und das Element 5 korrekt funktioniert.")
    void testRemoveFirstOccurrence() {
        var list = newList(REMOVE_OCCURRENCE_ARRAY);
        assertThat(Arrays.stream(LIST_CLASS.getActual().getDeclaredMethods()).filter(m -> m.getName().equals(REMOVE_FIRST_OCCURENCE_METHOD.getName())))
                .withFailMessage("Die Methode removeFirstOccurrence() wurde nicht gefunden. (Tippfehler im Methodenname?)").isNotEmpty();
        REMOVE_FIRST_OCCURENCE_METHOD.invoke(list, 5);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(8);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode removeLastOccurrence() für die Liste (5,6,5,7,5,8) und das Element 5 korrekt funktioniert.")
    void testRemoveLastOccurrence() {
        var list = newList(REMOVE_OCCURRENCE_ARRAY);
        assertThat(Arrays.stream(LIST_CLASS.getActual().getDeclaredMethods()).filter(m -> m.getName().equals(REMOVE_LAST_OCCURENCE_METHOD.getName())))
                .withFailMessage("Die Methode removeLastOccurrence() wurde nicht gefunden. (Tippfehler im Methodenname?)").isNotEmpty();
        REMOVE_LAST_OCCURENCE_METHOD.invoke(list, 5);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(6);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(8);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob die Methode sort() für die Liste (3,1,4,7,2,8,1,2) korrekt funktioniert.")
    void testSort() {
        var list = newList(SORT_ARRAY);
        SORT_METHOD.invoke(list);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(1);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(1);
        assertThat(GET_METHOD.invoke(list, 2)).isEqualTo(2);
        assertThat(GET_METHOD.invoke(list, 3)).isEqualTo(2);
        assertThat(GET_METHOD.invoke(list, 4)).isEqualTo(3);
        assertThat(GET_METHOD.invoke(list, 5)).isEqualTo(4);
        assertThat(GET_METHOD.invoke(list, 6)).isEqualTo(7);
        assertThat(GET_METHOD.invoke(list, 7)).isEqualTo(8);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode sort() für die Liste (5) korrekt funktioniert.")
    void testSortSingletonList() {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        ADD_METHOD.invoke(list, 5);
        SORT_METHOD.invoke(list);

        assertThat(GET_METHOD.invoke(list, 0)).isEqualTo(5);
        assertThat(GET_METHOD.invoke(list, 1)).isEqualTo(5);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode sort() für eine leere Liste korrekt funktioniert. (ruft auch length() auf)")
    void testSortEmptyList() {
        var list = DEFAULT_CONSTRUCTOR.invoke();
        SORT_METHOD.invoke(list);

        assertThat(LENGTH_METHOD.invoke(list)).isZero();
    }
}
