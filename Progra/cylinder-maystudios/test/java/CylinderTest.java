import de.hhu.educode.testing.ConstructorCall;
import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class CylinderTest {

    private static final RuntimeClass CYLINDER_CLASS = RuntimeClass.forName("Cylinder");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(CYLINDER_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[]{String[].class})
            .build();

    private static final ConstructorCall DEFAULT_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(CYLINDER_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final ConstructorCall CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(CYLINDER_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{double.class, double.class})
            .build();

    private static final MethodCall<Double> VOLUME_METHOD = MethodCall.<Double>builder()
            .runtimeClass(CYLINDER_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(double.class)
            .name("volume")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<String> TOSTRING_METHOD = MethodCall.<String>builder()
            .runtimeClass(CYLINDER_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(String.class)
            .name("toString")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Cylinder[]> SORT_METHOD = MethodCall.<Cylinder[]>builder()
            .runtimeClass(CYLINDER_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(Cylinder[].class)
            .name("sorted")
            .parameterTypes(new Class<?>[]{Cylinder[].class})
            .build();

    private static final String EMPTY_INPUT    = "";

    private static final Object[] MANY_ARGS    = { new String[] {"8.5", "2.4", "1.0", "1.0", "1.0", "4.0", "1.0", "3.0", "15.0", "2.0", "3.0", "7.0"} };

    private static final Cylinder FIRST  = (Cylinder)CONSTRUCTOR.invoke(1.0, 2.0);
    private static final Cylinder SECOND = (Cylinder)CONSTRUCTOR.invoke(3.0, 8.0);
    private static final Cylinder THIRD  = (Cylinder)CONSTRUCTOR.invoke(13.0, 4.0);
    private static final Object[] SORT_ARGS = { new Cylinder[]{ THIRD, FIRST, SECOND } };
    private static final Object[] SORT_ARGS2 = { new Cylinder[]{ THIRD, FIRST, SECOND } };
    private static final Object[] SORT_ARGS2_REF = { new Cylinder[]{ ((Cylinder[])(SORT_ARGS2[0]))[0], ((Cylinder[])(SORT_ARGS2[0]))[1], ((Cylinder[])(SORT_ARGS2[0]))[2] } };

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode volume() für Radius = 0 und Höhe = 0 funktioniert.")
    void testDefaultConstructor() {
        var cyliZero = DEFAULT_CONSTRUCTOR.invoke();
        var volumeResultZero = Program.execute(() -> VOLUME_METHOD.invoke(cyliZero));
        assertThat(volumeResultZero.getReturnValue().get()).isEqualTo(0.0, Offset.offset(0.00001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode volume() für Radius = 1.0 und Höhe = 3.2 funktioniert.")
    void testVolume() {
        var cylinder = CONSTRUCTOR.invoke(1.0, 3.2);
        var volumeResult = Program.execute(() -> VOLUME_METHOD.invoke(cylinder));
        assertThat(volumeResult.getReturnValue().get()).isEqualTo(10.05309649148733836308, Offset.offset(0.00001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode volume() für Radius = 2.1 und Höhe = 2.2 funktioniert.")
    void testVolume2() {
        var cylinder = CONSTRUCTOR.invoke(2.1, 2.2);
        var volumeResult = Program.execute(() -> VOLUME_METHOD.invoke(cylinder));
        assertThat(volumeResult.getReturnValue().get()).isEqualTo(30.47973192512817, Offset.offset(0.00001));
    }

    private static double extractRadius(String output) {
        return Double.parseDouble(output.toLowerCase().split("r=")[1].split(",")[0]);
    }

    private static double extractHeight(String output) {
        return Double.parseDouble(output.toLowerCase().split("h=")[1].split(",")[0]);
    }

    private static double extractVolume(String output) {
        return Double.parseDouble(output.toLowerCase().split("v=")[1].trim());
    }

    private static boolean checkStringOutput(String actual, String expected) {
        assertThat(actual.toLowerCase()).matches("cylinder: r=.*, h=.*, v=.*");
        assertThat(extractRadius(actual)).isCloseTo(extractRadius(expected), Offset.offset(0.001));
        assertThat(extractHeight(actual)).isCloseTo(extractHeight(expected), Offset.offset(0.001));
        assertThat(extractVolume(actual)).isCloseTo(extractVolume(expected), Offset.offset(0.001));
        return true;
    }

    private static boolean checkStringOutput(String[] actual, String[] expected) {
        assertThat(actual).hasSameSizeAs(expected);
        for(int i = 0; i < actual.length; i++) {
            checkStringOutput(actual[i], expected[i]);
        }
        return true;
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob die Methode toString() für Radius = 1.0 und Höhe = 3.2 funktioniert.")
    void testToString() {
        var cylinder = CONSTRUCTOR.invoke(1.0, 3.2);
        var result = Program.execute(() -> TOSTRING_METHOD.invoke(cylinder));
        assertThat(result.getReturnValue().get()).matches(output -> checkStringOutput(output, "Cylinder: r=1.0, h=3.2, V=10.05309649148733836308"));
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob sorted sortierte Zylinder zurückgibt")
    void testSortSorts() {
        var result = Program.execute(() -> SORT_METHOD.invoke(null, SORT_ARGS), EMPTY_INPUT);
        assertThat(result.getReturnValue()).isPresent();
        assertThat(result.getReturnValue().get()[0].volume()).isEqualTo(FIRST.volume());
        assertThat(result.getReturnValue().get()[0].toString()).isEqualTo(FIRST.toString());
        assertThat(result.getReturnValue().get()[1].volume()).isEqualTo(SECOND.volume());
        assertThat(result.getReturnValue().get()[2].volume()).isEqualTo(THIRD.volume());
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob sorted für leeres Array funktioniert")
    void testSortEmpty() {
        var result = Program.execute(() -> SORT_METHOD.invoke(null, new Object[]{new Cylinder[]{}}), EMPTY_INPUT);
        assertThat(result.getReturnValue()).isPresent();
        assertThat(result.getReturnValue().get()).isEmpty();
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob sorted das Original-Array nicht verändert")
    void testSortNoMutation() {
        var result = Program.execute(() -> SORT_METHOD.invoke(null, SORT_ARGS2), EMPTY_INPUT);
        assertThat(result.getReturnValue()).isPresent();
        // we may not expect that a shallow array copy is created
        assertThat(result.getReturnValue().get()[0].volume()).isEqualTo(FIRST.volume());
        assertThat(result.getReturnValue().get()[1].toString()).isEqualTo(SECOND.toString());
        assertThat(result.getReturnValue().get()[2].volume()).isEqualTo(THIRD.volume());
        // the original object must not be changed
        assertThat(((Cylinder[])SORT_ARGS2[0])[0]).isEqualTo(((Cylinder[])SORT_ARGS2_REF[0])[0]);
        assertThat(((Cylinder[])SORT_ARGS2[0])[0].toString()).isEqualTo(((Cylinder[])SORT_ARGS2_REF[0])[0].toString());
        assertThat(((Cylinder[])SORT_ARGS2[0])[1]).isEqualTo(((Cylinder[])SORT_ARGS2_REF[0])[1]);
        assertThat(((Cylinder[])SORT_ARGS2[0])[2]).isEqualTo(((Cylinder[])SORT_ARGS2_REF[0])[2]);
    }

    @Test
    @DisplayName("[3 Punkte] Prüfe, ob Konsolen-Ausgabe stimmt für Eingabe von: 8.5 2.4 1.0 1.0 1.0 4.0 1.0 3.0 15.0 2.0 3.0 7.0")
    void testMain() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, MANY_ARGS), EMPTY_INPUT);
        var lines = result.getOutput().split("\n");

        String[] expected = {
                "Cylinder: r=1.0, h=1.0, V=3.141592653589793",
                "Cylinder: r=1.0, h=3.0, V=9.42477796076938",
                "Cylinder: r=1.0, h=4.0, V=12.566370614359172",
                "Cylinder: r=3.0, h=7.0, V=197.92033717615697402315",
                "Cylinder: r=8.5, h=2.4, V=544.75216613247014754942",
                "Cylinder: r=15.0, h=2.0, V=1413.71669411540695730819"
        };

        assertThat(lines).matches(output -> checkStringOutput(output, expected));

    }
}
