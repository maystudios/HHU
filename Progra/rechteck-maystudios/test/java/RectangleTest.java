import de.hhu.educode.testing.ConstructorCall;
import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RectangleTest {

    private static final RuntimeClass RECTANGLE_CLASS = RuntimeClass.forName("Rectangle");

    private static final ConstructorCall DEFAULT_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(RECTANGLE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final ConstructorCall CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(RECTANGLE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{int.class, int.class})
            .build();

    private static final MethodCall<Integer> AREA_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(RECTANGLE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("area")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Integer> PERIMETER_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(RECTANGLE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("perimeter")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Boolean> IS_SQUARE_METHOD = MethodCall.<Boolean>builder()
            .runtimeClass(RECTANGLE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(boolean.class)
            .name("isSquare")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<String> STRING_METHOD = MethodCall.<String>builder()
            .runtimeClass(RECTANGLE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(String.class)
            .name("toString")
            .parameterTypes(new Class<?>[]{})
            .build();

    private Object getZeroSquare() {
        try {
            return DEFAULT_CONSTRUCTOR.invoke();
        } catch (Exception | AssertionError e) {
            return CONSTRUCTOR.invoke(0, 0);
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob auch ein zweiter Konstruktor ohne Parameter existiert und funktioniert.")
    void testZeroConstructor() {
        var rectangle = DEFAULT_CONSTRUCTOR.invoke();
        try {
            var result = Program.execute(() -> STRING_METHOD.invoke(rectangle));
            assertThat(result.getReturnValue().orElse("no return value")).isBlank();
        } catch (Exception | AssertionError e) {
            try {
                var areaResult = Program.execute(() -> AREA_METHOD.invoke(rectangle));
                assertThat(areaResult.getReturnValue()).contains(0);
            } catch (Exception | AssertionError e2) {
                try {
                    var squareResult = Program.execute(() -> IS_SQUARE_METHOD.invoke(rectangle));
                    assertThat(squareResult.getReturnValue()).contains(true);
                } catch (Exception | AssertionError e3) {
                    try {
                        var perimeterResult = Program.execute(() -> PERIMETER_METHOD.invoke(rectangle));
                        assertThat(perimeterResult.getReturnValue()).contains(0);
                    } catch(Exception | AssertionError e4) {
                        fail("keine der Methoden toString, area, perimeter und isSquare liefern für ein 0×0-Quadrat die richtigen Werte");
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, dass es nur private Instanzvariablen gibt.")
    void testPrivateVariables() {
        Arrays.stream(RECTANGLE_CLASS.getActual().getDeclaredFields())
                .filter(field -> !Modifier.isPrivate(field.getModifiers()) && !(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())))
                .findAny()
                .ifPresent(field -> fail("Variable »" + field.getName() + "« ist nicht privat; Schlüsselwort »private« vergessen?"));
        Arrays.stream(RECTANGLE_CLASS.getActual().getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .findAny()
                .orElseGet(() -> fail("Es gibt keine privaten Instanzvariablen in der Klasse »Rectangle«."));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methoden area() und perimeter() für die Größe 0×0 funktionieren.")
    void testZeroZeroAreaPerimeter() {
        var rectangle = getZeroSquare();
        var areaResult = Program.execute(() -> AREA_METHOD.invoke(rectangle));
        var perimeterResult = Program.execute(() -> PERIMETER_METHOD.invoke(rectangle));

        assertThat(areaResult.getReturnValue()).contains(0);
        assertThat(perimeterResult.getReturnValue()).contains(0);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methode perimeter() für die Größe 8×5 funktioniert.")
    void testEightFivePerimeter() {
        var rectangle = CONSTRUCTOR.invoke(8, 5);
        var areaResult = Program.execute(() -> AREA_METHOD.invoke(rectangle));
        var perimeterResult = Program.execute(() -> PERIMETER_METHOD.invoke(rectangle));

        assertThat(perimeterResult.getReturnValue()).contains(26);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Methoden area() für die Größe 8×5 funktioniert.")
    void testEightFiveArea() {
        var rectangle = CONSTRUCTOR.invoke(8, 5);
        var areaResult = Program.execute(() -> AREA_METHOD.invoke(rectangle));
        var perimeterResult = Program.execute(() -> PERIMETER_METHOD.invoke(rectangle));

        assertThat(areaResult.getReturnValue()).contains(40);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob die Methode isSquare für ein Quadrat und ein Rechteck funktioniert.")
    void testIsSquareTrue() {
        // Absicht, dass beides zusammen getestet wird, so bekommen statische "Instanz"variablen einen größeren Abzug
        var rectangle1 = CONSTRUCTOR.invoke(4, 4);
        var rectangle2 = CONSTRUCTOR.invoke(8, 5);
        var result1 = Program.execute(() -> IS_SQUARE_METHOD.invoke(rectangle1));
        var result2 = Program.execute(() -> IS_SQUARE_METHOD.invoke(rectangle2));
        assertThat(result1.getReturnValue()).contains(true);
        assertThat(result2.getReturnValue()).contains(false);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Zeichnung eines 0×0-Rechtecks leer ist.")
    void testZeroZeroDraw() {
        var rectangle = getZeroSquare();
        var result = Program.execute(() -> STRING_METHOD.invoke(rectangle));

        assertThat(result.getReturnValue().orElse("no return value")).isBlank();
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die Zeichnung eines 8×5-Rechtecks korrekt ist.")
    void testEightFiveDrawStdoutOrReturnValue() {
        // gibt nen Punkt, selbst wenn die Zeichnung auf die Standardausgabe geht
        var rectangle = CONSTRUCTOR.invoke(8, 5);
        var result = Program.execute(() -> STRING_METHOD.invoke(rectangle));

        var output = result.getOutput().isEmpty() ? result.getReturnValue().orElse("no return value") : result.getOutput();

        assertThat(output.lines().map(String::trim)).containsExactly(
                "* * * * * * * *",
                "* * * * * * * *",
                "* * * * * * * *",
                "* * * * * * * *",
                "* * * * * * * *");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob toString die Zeichnung eines 8×5-Rechtecks korrekt zurückgibt.")
    void testEightFiveDrawReturnValue() {
        var rectangle = CONSTRUCTOR.invoke(8, 5);
        var result = Program.execute(() -> STRING_METHOD.invoke(rectangle));

        assertThat(result.getOutput())
                .withFailMessage("toString soll nicht auf die Standardausgabe schreiben, sondern die *-Repräsentation als String zurückliefern. Inhalt der Standardausgabe:\n%s",
                        result.getOutput()).isEmpty();

        assertThat(result.getReturnValue().orElse("no return value").lines().map(String::trim)).containsExactly(
                "* * * * * * * *",
                "* * * * * * * *",
                "* * * * * * * *",
                "* * * * * * * *",
                "* * * * * * * *");
    }
}
