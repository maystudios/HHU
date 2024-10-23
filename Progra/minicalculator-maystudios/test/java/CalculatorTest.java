import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    private static final RuntimeClass LIST_CLASS = RuntimeClass.forName("Calculator");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(LIST_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private Object[] args(String ...args) {
        return new Object[]{args};
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die vier Rechenarten mit Ganzzahlen funktionieren")
    void testInteger() {
        var results = new Program.ExecutionResult[]{
                Program.execute(() -> MAIN_METHOD.invoke(null, args("1", "+", "3"))),
                Program.execute(() -> MAIN_METHOD.invoke(null, args("1", "-", "3"))),
                Program.execute(() -> MAIN_METHOD.invoke(null, args("2", "*", "3"))),
                Program.execute(() -> MAIN_METHOD.invoke(null, args("27", "/", "3"))),
        };
        assertThat(Double.parseDouble(results[0].getOutput().trim())).isCloseTo(4, Offset.offset(0.001));
        assertThat(Double.parseDouble(results[1].getOutput().trim())).isCloseTo(-2, Offset.offset(0.001));
        assertThat(Double.parseDouble(results[2].getOutput().trim())).isCloseTo(6, Offset.offset(0.001));
        assertThat(Double.parseDouble(results[3].getOutput().trim())).isCloseTo(9, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob die vier Rechenarten mit Kommazahlen funktionieren")
    void testDouble() {
        var results = new Program.ExecutionResult[]{
                Program.execute(() -> MAIN_METHOD.invoke(null, args("1.1", "+", "3"))),
                Program.execute(() -> MAIN_METHOD.invoke(null, args("-1.1", "-", "3"))),
                Program.execute(() -> MAIN_METHOD.invoke(null, args("2.5", "*", "3"))),
                Program.execute(() -> MAIN_METHOD.invoke(null, args("5000", "/", "2000"))),
                Program.execute(() -> MAIN_METHOD.invoke(null, args("0", "/", "2000"))),
        };
        assertThat(Double.parseDouble(results[0].getOutput().trim())).isCloseTo(4.1, Offset.offset(0.001));
        assertThat(Double.parseDouble(results[1].getOutput().trim())).isCloseTo(-4.1, Offset.offset(0.001));
        assertThat(Double.parseDouble(results[2].getOutput().trim())).isCloseTo(7.5, Offset.offset(0.001));
        assertThat(Double.parseDouble(results[3].getOutput().trim())).isCloseTo(2.5, Offset.offset(0.001));
        assertThat(Double.parseDouble(results[4].getOutput().trim())).isCloseTo(0, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei zu wenigen Argumenten eine Fehlermeldung ausgegeben wird")
    void testTooFewArguments() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, args("1", "+")));
        assertThat(result.getOutput().trim()).contains("too few arguments");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei zu vielen Argumenten eine Fehlermeldung ausgegeben wird")
    void testTooManyArguments() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, args("1", "+", "1", "1")));
        assertThat(result.getOutput().trim()).contains("too many arguments");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei einer unbekannten Rechenoperation eine Fehlermeldung ausgegeben wird")
    void testUnknownOperator() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, args("1", "^", "1")));
        assertThat(result.getOutput().trim()).contains("unknown operator");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe Fehlmeldung, wenn erstes Argument keine Zahl ist")
    void testFirstOperandNoNumber() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, args("eins", "+", "1")));
        assertThat(result.getOutput().trim()).contains("first operand is no number");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe Fehlmeldung, wenn zweites Argument keine Zahl ist")
    void testSecondOperandNoNumber() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, args("2", "+", "två")));
        assertThat(result.getOutput().trim()).contains("second operand is no number");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe Fehlmeldung bei Division durch Null")
    void testDivisionByZero() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, args("2", "/", "0")));
        assertThat(result.getOutput().trim()).contains("division by zero");
    }

}
