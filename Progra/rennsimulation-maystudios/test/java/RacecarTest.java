import de.hhu.educode.testing.ConstructorCall;
import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RacecarTest {

    private static final RuntimeClass RACECAR_CLASS = RuntimeClass.forName("Racecar");
    private static final RuntimeClass RACE_CLASS = RuntimeClass.forName("Race");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(RACE_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[]{String[].class})
            .build();

    private static final ConstructorCall DEFAULT_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(RACECAR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Void> ACCELERATE_METHOD = MethodCall.<Void>builder()
            .runtimeClass(RACECAR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("accelerate")
            .parameterTypes(new Class<?>[]{double.class})
            .build();

    private static final MethodCall<Void> DECELERATE_METHOD = MethodCall.<Void>builder()
            .runtimeClass(RACECAR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("decelerate")
            .parameterTypes(new Class<?>[]{double.class})
            .build();

    private static final MethodCall<Void> DRIVE_METHOD = MethodCall.<Void>builder()
            .runtimeClass(RACECAR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("drive")
            .parameterTypes(new Class<?>[]{double.class})
            .build();

    private static final MethodCall<Double> GET_SPEED_METHOD = MethodCall.<Double>builder()
            .runtimeClass(RACECAR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(double.class)
            .name("getSpeed")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Double> GET_DISTANCE_METHOD = MethodCall.<Double>builder()
            .runtimeClass(RACECAR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(double.class)
            .name("getDistance")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<String> TO_STRING_METHOD = MethodCall.<String>builder()
            .runtimeClass(RACECAR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(String.class)
            .name("toString")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final Object[] EMPTY_ARGS = { new String[]{} };

    private static final String EMPTY_INPUT = "";

    private static final String ACCELERATE_DRIVE_INPUT =
            "accelerate 10\n" +
            "drive 5";
    private static final String[] ACCELERATE_DRIVE_RESULT = new String[]{
            "Speed: 10.0m/s, Distance: 0.0m",
            "Speed: 10.0m/s, Distance: 50.0m"
    };

    private static final String ACCELERATE_DECELERATE_DRIVE_INPUT =
            """
                    accelerate 10
                    decelerate 5
                    drive 5""";
    private static final String[] ACCELERATE_DECELERATE_DRIVE_RESULT = new String[]{
            "Speed: 10.0m/s, Distance: 0.0m",
            "Speed: 5.0m/s, Distance: 0.0m",
            "Speed: 5.0m/s, Distance: 25.0m"
    };

    private static final String MULTIPLE_INPUT =
            """
                    accelerate 20.5
                    drive 8.0
                    decelerate 5.7
                    drive 12.3
                    decelerate 1.5
                    drive 2.6
                    accelerate 31.8
                    drive 11.3""";
    private static final String[] MULTIPLE_RESULT = new String[]{
            "Speed: 20.5m/s, Distance: 0.0m",
            "Speed: 20.5m/s, Distance: 164.0m",
            "Speed: 14.8m/s, Distance: 164.0m",
            "Speed: 14.8m/s, Distance: 346.04m",
            "Speed: 13.3m/s, Distance: 346.04m",
            "Speed: 13.3m/s, Distance: 380.62m",
            "Speed: 45.1m/s, Distance: 380.62m",
            "Speed: 45.1m/s, Distance: 890.25m"
    };

    private static double extractSpeed(String output) {
        return Double.parseDouble(output.split("Speed: ")[1].split("m/s")[0]);
    }

    private static double extractDistance(String output) {
        return Double.parseDouble(output.split("Distance: ")[1].split("m")[0]);
    }

    private static boolean checkStringOutput(String actual, String expected) {
        assertThat(actual).matches("Speed: .*m/s, Distance: .*m");
        assertThat(extractSpeed(actual)).isCloseTo(extractSpeed(expected), Offset.offset(0.001));
        assertThat(extractDistance(actual)).isCloseTo(extractDistance(expected), Offset.offset(0.001));
        return true;
    }

    private static boolean checkStringOutput(List<? extends java.lang.String> actual, String[] expected) {
        assertThat(actual).hasSameSizeAs(expected);
        for(int i = 0; i < actual.size(); i++) {
            checkStringOutput(actual.get(i), expected[i]);
        }
        return true;
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, dass es nur private Instanzvariablen gibt.")
    void testPrivateVariables() {
        Arrays.stream(RACECAR_CLASS.getActual().getDeclaredFields())
                .filter(field -> !Modifier.isPrivate(field.getModifiers()) && !(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())))
                .findAny()
                .ifPresent(field -> fail("Variable »" + field.getName() + "« ist nicht privat; Schlüsselwort »private« vergessen?"));
        Arrays.stream(RACECAR_CLASS.getActual().getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .findAny()
                .orElseGet(() -> fail("Es gibt keine privaten Instanzvariablen in der Klasse »Racecar«."));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Klasse die geforderten Methoden besitzt.")
    void testMethodsExist() {
        assertThat(RACECAR_CLASS.getActual()).hasPublicMethods("getSpeed", "getDistance", "accelerate", "decelerate", "drive");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Beschleunigen korrekt funktioniert.")
    void testSpeedAfterAccelerate() {
        var racecar = DEFAULT_CONSTRUCTOR.invoke();
        ACCELERATE_METHOD.invoke(racecar, 5.5);
        assertThat(GET_SPEED_METHOD.invoke(racecar)).isCloseTo(5.5, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Beschleunigen mit negativem Parameter wie gewünscht nichts tut.")
    void testSpeedAfterNegativeAcceleration() {
        var racecar = DEFAULT_CONSTRUCTOR.invoke();
        ACCELERATE_METHOD.invoke(racecar, -5.5);
        assertThat(GET_SPEED_METHOD.invoke(racecar)).isCloseTo(0, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Bremsen nach Beschleunigung wie gewünscht funktioniert.")
    void testSpeedAfterDecelerate() {
        var racecar = DEFAULT_CONSTRUCTOR.invoke();
        ACCELERATE_METHOD.invoke(racecar, 20);
        DECELERATE_METHOD.invoke(racecar, 10);
        DECELERATE_METHOD.invoke(racecar, -5.5);
        assertThat(GET_SPEED_METHOD.invoke(racecar)).isCloseTo(10, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Fahren und Abfragen der Geschwindigkeit funktionieren.")
    void testDistance() {
        var racecar = DEFAULT_CONSTRUCTOR.invoke();
        ACCELERATE_METHOD.invoke(racecar, 2);
        DRIVE_METHOD.invoke(racecar, 2);
        assertThat(GET_DISTANCE_METHOD.invoke(racecar)).isCloseTo(4, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob toString funktioniert.")
    void testToString() {
        var racecar = DEFAULT_CONSTRUCTOR.invoke();
        assertThat(TO_STRING_METHOD.invoke(racecar)).matches(s -> checkStringOutput(s, "Speed: 0.0m/s, Distance: 0.0m"));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm bei einer leeren Eingabe nichts ausgibt.")
    void testEmptyInput() {
        Locale.setDefault(Locale.ENGLISH);
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), EMPTY_INPUT);

        assertThat(result.getOutput()).isBlank();
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm bei einmaligem Beschleunigen und Fahren die korrekte Ausgabe erzeugt.")
    void testAccelerateDrive() {
        Locale.setDefault(Locale.ENGLISH);
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), ACCELERATE_DRIVE_INPUT);

        assertThat(result.getOutput().lines()).matches(output -> checkStringOutput(output, ACCELERATE_DRIVE_RESULT));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Programm bei einmaligem Beschleunigen mit anschließendem Bremsen und Fahren die korrekte Ausgabe erzeugt.")
    void testAccelerateDecelerateDrive() {
        Locale.setDefault(Locale.ENGLISH);
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), ACCELERATE_DECELERATE_DRIVE_INPUT);

        assertThat(result.getOutput().lines()).matches(output -> checkStringOutput(output, ACCELERATE_DECELERATE_DRIVE_RESULT));

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), MULTIPLE_INPUT);

        assertThat(result2.getOutput().lines()).matches(output -> checkStringOutput(output, MULTIPLE_RESULT));
    }
}
