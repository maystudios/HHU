import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

class CodewortTest {

    private static final RuntimeClass CODEWORTCLASS = RuntimeClass.forName("Codewort");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(CODEWORTCLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final Object[] EMPTY_ARGS = { new String[]{} };

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Ausgabe das Codewort oder alternativ 'nicht abgegeben' enthält.")
    void testOutputContainsCodewort() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS));
        assertThat(result.getOutput().trim().toLowerCase()).containsAnyOf(
                "Wie Sie herausfinden können, welche Testfälle bei einer Abgabe nicht durchlaufen und wie Sie nachbessern können, ist hier erklärt: https://hsp.pages.cs.uni-duesseldorf.de/programmierung/website/lectures/progra/tutorials/classroom/#nachbessern".toLowerCase(),
                "nicht abgegeben",
                "#nachbessern",
                "tutorials/classroom",
                "Testfälle".toLowerCase());
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Ausgabe den gesuchten Benutzernamen enthält.")
    void testOutputContainsUsername() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS));
        assertThat(result.getOutput().trim().toLowerCase()).contains("mabre");
    }

}
