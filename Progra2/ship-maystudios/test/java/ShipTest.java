import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.RuntimeClass;

import java.lang.reflect.Modifier;

class ShipTest {

    private static final RuntimeClass SHIP_CLASS = RuntimeClass.forName("Ship");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(SHIP_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();
    
    @Test
    @DisplayName("Teste, ob Ship existiert, kompiliert und eine main-Methode hat.")
    void testShipExists() {
        assertThat(MAIN_METHOD.getRuntimeClass().getActual().getName()).isEqualTo("Ship");
    }

}
