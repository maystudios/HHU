import de.hhu.educode.testing.ConstructorCall;
import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.RuntimeClass;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ResistorsTest {

    private static final RuntimeClass RESISTOR_INTERFACE = RuntimeClass.forName("Resistor");
    private static final RuntimeClass SINGLE_RESISTOR_CLASS = RuntimeClass.forName("SingleResistor");
    private static final RuntimeClass SERIES_CLASS = RuntimeClass.forName("SeriesCircuit");
    private static final RuntimeClass PARALLEL_CLASS = RuntimeClass.forName("ParallelCircuit");

    private static final ConstructorCall SINGLE_RESISTOR_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(SINGLE_RESISTOR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{double.class})
            .build();

    private static final MethodCall<Integer> SINGLE_COUNT_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(SINGLE_RESISTOR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("resistorCount")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Double> SINGLE_RESISTANCE_METHOD = MethodCall.<Double>builder()
            .runtimeClass(SINGLE_RESISTOR_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(double.class)
            .name("resistance")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final ConstructorCall PARALLEL_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(PARALLEL_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{RESISTOR_INTERFACE.getActual(), RESISTOR_INTERFACE.getActual()})
            .build();

    private static final MethodCall<Integer> PARALLEL_COUNT_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(PARALLEL_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("resistorCount")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Double> PARALLEL_RESISTANCE_METHOD = MethodCall.<Double>builder()
            .runtimeClass(PARALLEL_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(double.class)
            .name("resistance")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final ConstructorCall SERIES_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(SERIES_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{RESISTOR_INTERFACE.getActual(), RESISTOR_INTERFACE.getActual()})
            .build();

    private static final MethodCall<Integer> SERIES_COUNT_METHOD = MethodCall.<Integer>builder()
            .runtimeClass(SERIES_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(int.class)
            .name("resistorCount")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final MethodCall<Double> SERIES_RESISTANCE_METHOD = MethodCall.<Double>builder()
            .runtimeClass(SERIES_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(double.class)
            .name("resistance")
            .parameterTypes(new Class<?>[]{})
            .build();

    private static final Object[] EMPTY_ARGS = { new String[]{} };

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob das Interface die geforderten Methoden beinhaltet")
    void testMethodsExist() {
        assertThat(RESISTOR_INTERFACE.getActual()).hasPublicMethods("resistance", "resistorCount");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob ein einzelner Widerstand angibt, aus einem Widerstand zu bestehen")
    void testSingleCount() {
        var resistor = SINGLE_RESISTOR_CONSTRUCTOR.invoke(2);
        assertThat(SINGLE_COUNT_METHOD.invoke(resistor)).isEqualTo(1);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob ein einzelner Widerstand seinen Widerstandswert korrekt wiedergibt")
    void testSingleResistance() {
        var resistor = SINGLE_RESISTOR_CONSTRUCTOR.invoke(2);
        assertThat(SINGLE_RESISTANCE_METHOD.invoke(resistor)).isCloseTo(2, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Parallelschaltung aus 2 Widerständen richtig angibt, aus zwei einzelnen Widerständen zu bestehen")
    void testParallelCount() {
        var resistor1 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(2);
        var resistor2 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(4);
        var parallelCircuit = PARALLEL_CONSTRUCTOR.invoke(resistor1, resistor2);
        assertThat(PARALLEL_COUNT_METHOD.invoke(parallelCircuit)).isEqualTo(2);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Parallelschaltung aus 2 Widerständen ihren Gesamtwiderstand richtig berechnet")
    void testParallelResistance() {
        var resistor1 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(2);
        var resistor2 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(4);
        var parallelCircuit = PARALLEL_CONSTRUCTOR.invoke(resistor1, resistor2);
        assertThat(PARALLEL_RESISTANCE_METHOD.invoke(parallelCircuit)).isCloseTo(2. * 4 / (2 + 4), Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Reihenschaltung aus 3 Widerständen richtig angibt, aus 3 einzelnen Widerständen zu bestehen.")
    void testSeriesCount() {
        var resistor1 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(2);
        var resistor2 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(4);
        var resistor3 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(4.5);
        var seriesCircuit = SERIES_CONSTRUCTOR.invoke(SERIES_CONSTRUCTOR.invoke(resistor1, resistor2), resistor3);
        assertThat(SERIES_COUNT_METHOD.invoke(seriesCircuit)).isEqualTo(3);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Reihenschaltung aus 3 Widerständen ihren Gesamtwiderstand richtig berechnet")
    void testSeriesResistance() {
        var resistor1 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(2);
        var resistor2 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(4);
        var resistor3 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(4.5);
        var seriesCircuit = SERIES_CONSTRUCTOR.invoke(SERIES_CONSTRUCTOR.invoke(resistor1, resistor2), resistor3);
        assertThat(SERIES_RESISTANCE_METHOD.invoke(seriesCircuit)).isCloseTo(10.5, Offset.offset(0.001));
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei der großen Schaltung auf dem Blatt korrekt 7 als Anzahl der Einzelwiderstände zurückgegeben wird")
    void testBigCount() {
        var resistor1 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(100);
        var resistor2 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(200);
        var resistor3 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(300);
        var resistor4 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(400);
        var resistor5 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(500);
        var resistor6 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(600);
        var resistor7 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(700);
        var upperBranch = SERIES_CONSTRUCTOR.invoke(resistor1, PARALLEL_CONSTRUCTOR.invoke(resistor2, resistor3));
        var lowerBranch = SERIES_CONSTRUCTOR.invoke(PARALLEL_CONSTRUCTOR.invoke(resistor4, resistor5), resistor6);
        var circuit = SERIES_CONSTRUCTOR.invoke(PARALLEL_CONSTRUCTOR.invoke(upperBranch, lowerBranch), resistor7);
        assertThat(SERIES_COUNT_METHOD.invoke(circuit)).isEqualTo(7);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob bei der großen Schaltung auf dem Blatt korrekt 873,56 Ohm berechnet wird")
    void testBigResistance() {
        var resistor1 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(100);
        var resistor2 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(200);
        var resistor3 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(300);
        var resistor4 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(400);
        var resistor5 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(500);
        var resistor6 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(600);
        var resistor7 = SINGLE_RESISTOR_CONSTRUCTOR.invoke(700);
        var upperBranch = SERIES_CONSTRUCTOR.invoke(resistor1, PARALLEL_CONSTRUCTOR.invoke(resistor2, resistor3));
        var lowerBranch = SERIES_CONSTRUCTOR.invoke(PARALLEL_CONSTRUCTOR.invoke(resistor4, resistor5), resistor6);
        var circuit = SERIES_CONSTRUCTOR.invoke(PARALLEL_CONSTRUCTOR.invoke(upperBranch, lowerBranch), resistor7);
        assertThat(SERIES_RESISTANCE_METHOD.invoke(circuit)).isCloseTo(873.56, Offset.offset(0.01));
    }

}
