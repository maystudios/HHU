import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ZulassungTest {

    private static final RuntimeClass ZULASSUNG_CLASS = RuntimeClass.forName("Zulassung");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(ZULASSUNG_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[]{String[].class})
            .build();

    private static final Object[] ARGS_200 = { new String[]{"200"} };
    private static final Object[] ARGS_120 = { new String[]{"120"} };

    private static final String SINGLE_INPUT_4_ZUGELASSEN = "Max Mustermann,25,25,25,25";
    private static final String[] SINGLE_RESULT_4_ZUGELASSEN = new String[]{"Max Mustermann,true,true,true"};
    private static final String SINGLE_INPUT_4_NICHT_ZUGELASSEN = "Max Mustermann,25,25,25,24";
    private static final String[] SINGLE_RESULT_4_NICHT_ZUGELASSEN = new String[]{"Max Mustermann,true,true,false"};

    private static final String SINGLE_INPUT_3_ZUGELASSEN = "Max Mustermann,50,25,25";
    private static final String[] SINGLE_RESULT_3_ZUGELASSEN = new String[]{"Max Mustermann,true,true,true"};
    private static final String SINGLE_INPUT_3_NICHT_ZUGELASSEN = "Max Mustermann,39,25,15";
    private static final String[] SINGLE_RESULT_3_NICHT_ZUGELASSEN = new String[]{"Max Mustermann,false,true,false"};

    private static final String MULTIPLE_INPUT =
            "Tai Becker,2,10,13,3,4,5,7,18\n" +
            "Sascha Maier,0,0,0,20,17,20,18,2\n" +
            "Kim Müller,20,20,18,20,10,0,0,19\n" +
            "Kari Nguyen-Kim,1,10,15,4,8,0,9,12\n" +
            "Katara Schmidt,0,0,0,20,17,20,20,20";
    private static final String[] MULTIPLE_RESULT = new String[]{
            "Tai Becker,true,true,true",
            "Sascha Maier,false,true,false",
            "Kim Müller,true,true,true",
            "Kari Nguyen-Kim,true,true,false",
            "Katara Schmidt,false,true,false"};

    @Test
    @DisplayName("Prüfe, ob das Programm bei einer leeren Eingabe nichts ausgibt.")
    void testEmptyInput() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_120), "");

        assertThat(result.getOutput()).isBlank();
    }

    @Test
    @DisplayName("Prüfe, ob das Programm für eine einzelne Eingabezeile funktioniert.")
    void testSingleStudent() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_200), SINGLE_INPUT_4_ZUGELASSEN);
        assertThat(result.getOutput().lines()).containsExactly(SINGLE_RESULT_4_ZUGELASSEN);

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_200), SINGLE_INPUT_4_NICHT_ZUGELASSEN);
        assertThat(result2.getOutput().lines()).containsExactly(SINGLE_RESULT_4_NICHT_ZUGELASSEN);
    }

    @Test
    @DisplayName("Prüfe, ob das Programm für eine einzelne Eingabezeile mit ungeraden Punktegrenzen funktioniert.")
    void testSingleStudentUneven() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_200), SINGLE_INPUT_3_ZUGELASSEN);
        assertThat(result.getOutput().lines()).containsExactly(SINGLE_RESULT_3_ZUGELASSEN);

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_200), SINGLE_INPUT_3_NICHT_ZUGELASSEN);
        assertThat(result2.getOutput().lines()).containsExactly(SINGLE_RESULT_3_NICHT_ZUGELASSEN);
    }

    @Test
    @DisplayName("Prüfe, ob das Programm für mehrere Studis funktioniert.")
    void testMultipleStudents() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_120), MULTIPLE_INPUT);
        assertThat(result.getOutput().lines()).containsExactly(MULTIPLE_RESULT);
    }
}
