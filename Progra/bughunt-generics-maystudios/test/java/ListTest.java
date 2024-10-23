import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest {

    private static final RuntimeClass LIST_CLASS = RuntimeClass.forName("List");
    private static final RuntimeClass PLAYGROUND_CLASS = RuntimeClass.forName("ListPlayground");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(PLAYGROUND_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob die Konstruktion einer List<Integer> funktioniert und Elemente hinzugefügt und abgefragt werden können")
    void testList() {
        var list = new List<Integer>();
        list.prepend(1);
        list.prepend(2);
        assertThat(list.get(0)).isEqualTo(2);
        assertThat(list.get(1)).isEqualTo(1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob ListPlayground Ausgaben macht")
    void testListPlayground() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, (Object) null));
        assertThat(result.getOutput().lines().findFirst().orElse("")).startsWith("Die Länge der Liste ist:");
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob ListPlayground die richtigen Ausgaben macht")
    void tesOutput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, (Object) null));
        assertThat(result.getOutput().lines().map(String::trim)).containsExactly(
                "Die Länge der Liste ist: 3",
                "Das erste Element ist: Hallo",
                "Das Element mit Index 2 ist: !",
                "Das letzte Element ist: !",
                "Die gesamte Liste ist: Hallo -> Welt -> ! ->",
                "Und nochmal die ganze Liste: Hallo -> Welt -> ! ->");
    }

}
