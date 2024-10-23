import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;


public class SkytaleTest {

    private static final RuntimeClass SKYTALE_CLASS = RuntimeClass.forName("Skytale");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
        .runtimeClass(SKYTALE_CLASS)
        .modifiers(Modifier.PUBLIC | Modifier.STATIC)
        .returnType(void.class)
        .name("main")
        .parameterTypes(new Class<?>[] { String[].class })
        .build();

    private static final MethodCall<char[][]> ZEILENWEISE_SCHREIBEN_METHOD = MethodCall.<char[][]>builder()
            .runtimeClass(SKYTALE_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(char[][].class)
            .name("zeilenweiseInRasterSchreiben")
            .parameterTypes(new Class<?>[] { String.class })
            .build();

    private static final MethodCall<char[][]> SPALTENWEISE_SCHREIBEN_METHOD = MethodCall.<char[][]>builder()
            .runtimeClass(SKYTALE_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(char[][].class)
            .name("spaltenweiseInRasterSchreiben")
            .parameterTypes(new Class<?>[] { String.class })
            .build();

    private static final MethodCall<String> ZEILENWEISE_LESEN_METHOD = MethodCall.<String>builder()
            .runtimeClass(SKYTALE_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(String.class)
            .name("rasterZeilenweiseAblesen")
            .parameterTypes(new Class<?>[] { char[][].class })
            .build();

    private static final MethodCall<String> SPALTENWEISE_LESEN_METHOD = MethodCall.<String>builder()
            .runtimeClass(SKYTALE_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(String.class)
            .name("rasterSpaltenweiseAblesen")
            .parameterTypes(new Class<?>[] { char[][].class })
            .build();

    private static final MethodCall<String> ENCRYPT_METHOD = MethodCall.<String>builder()
            .runtimeClass(SKYTALE_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(String.class)
            .name("verschluesseln")
            .parameterTypes(new Class<?>[] { String.class })
            .build();

    private static final MethodCall<String> DECRYPT_METHOD = MethodCall.<String>builder()
            .runtimeClass(SKYTALE_CLASS)
            .modifiers(Modifier.PRIVATE | Modifier.STATIC)
            .returnType(String.class)
            .name("entschluesseln")
            .parameterTypes(new Class<?>[] { String.class })
            .build();
	
    private static final Object[] EMPTY_ARGS = { new String[]{} };
    private static final Object[] TOO_FEW_ARGS = { new String[]{"E"} };
    private static final Object[] WRONG_FIRST_ARGS = { new String[]{"-X", "WIR GREIFEN MORGEN MITTAG AN"} };
    private static final Object[] WRONG_SECOND_ARGS = { new String[]{"E", "WIR GREIFEN HEUTE UND MORGEN MITTAG AN"} };
    private static final Object[] INPUT_ENCR = { new String[]{ "E", "WIR GREIFEN MORGEN MITTAG AN"} };
    private static final Object[] INPUT_DECR = { new String[]{ "D", "WIRTIFGTREEA NNGG   RMMAEOIN"} };
    private static final Object[] INPUT_ENCR_ROM = { new String[]{ "E", "WIR GREIFEN HEUTE MAL ROM AN"} };
    private static final Object[] INPUT_DECR_ROM = { "WIU IFTRREEO N MG M RHAAEELN", 7, 4 };
	
    private static final String EMPTY_INPUT = "";
    private static final String SECRET_MSG               = "WIR GREIFEN MORGEN MITTAG AN";
    private static final String SECRET_MSG_ROM           = "WIR GREIFEN HEUTE MAL ROM AN";
    private static final String SECRET_TOOLONG_MSG       = "WIR GREIFEN HEUTE UND MORGEN MITTAG AN";
    private static final String SECRET_TOOSHORT_MSG       = "WIR GREIFEN HEUTE UND MORGEN MITTAG AN";
    private static final String ENCRYPTED_SECRET_MSG     = "WIRTIFGTREEA NNGG   RMMAEOIN";
    private static final String ENCRYPTED_SECRET_MSG_ROM = "WIU IFTRREEO N MG M RHAAEELN";

    private static final char[][] ROM_ZEILENWEISE_EINGETRAGEN = {
            "WIR GRE".toCharArray(),
            "IFEN HE".toCharArray(),
            "UTE MAL".toCharArray(),
            " ROM AN".toCharArray()};

    private static boolean methodExists(MethodCall<?> methodCall) {
        Class clazz = methodCall.getRuntimeClass().getActual();
        try {
            clazz.getDeclaredMethod(methodCall.getName(), methodCall.getParameterTypes());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob zeilenweiseInRasterSchreiben korrekt funktioniert (alternativ: Teste Verschlüsselung via main-Methode)")
    void testZeilenweiseInRaster() {
        if(methodExists(ZEILENWEISE_SCHREIBEN_METHOD) && !mainMethodWorks(INPUT_ENCR, ENCRYPTED_SECRET_MSG)) {
            var result = Program.execute(() -> ZEILENWEISE_SCHREIBEN_METHOD.invoke(null, SECRET_MSG_ROM), EMPTY_INPUT);
            assertThat(result.getReturnValue().get()).describedAs("zeilenweiseInRasterSchreiben(String) gefunden, Verhalten entspricht aber nicht unserer Spezifikation")
                    .isEqualTo(ROM_ZEILENWEISE_EINGETRAGEN);
        } else {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_ENCR), EMPTY_INPUT);
            assertThat(result.getOutput().trim()).describedAs("zeilenweiseInRasterSchreiben(String) nicht gefunden, stattdessen Verschlüsselung via main-Methode getestet").isEqualToIgnoringNewLines(ENCRYPTED_SECRET_MSG);
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob spaltenweiseInRasterSchreiben korrekt einliest (alternativ: Teste Verschlüsselung via main-Methode)")
    void testSpaltenweiseInRaster() {
        if(methodExists(SPALTENWEISE_SCHREIBEN_METHOD) && !mainMethodWorks(INPUT_ENCR, ENCRYPTED_SECRET_MSG)) {
            var result = Program.execute(() -> SPALTENWEISE_SCHREIBEN_METHOD.invoke(null, ENCRYPTED_SECRET_MSG_ROM), EMPTY_INPUT);
            assertThat(result.getReturnValue().get()).describedAs("spaltenweiseInRasterSchreiben(String) gefunden, Verhalten entspricht aber nicht unserer Spezifikation")
                    .isEqualTo(ROM_ZEILENWEISE_EINGETRAGEN);
        } else {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_ENCR), EMPTY_INPUT);
            assertThat(result.getOutput().trim()).describedAs("spaltenweiseInRasterSchreiben(String) nicht gefunden, stattdessen Verschlüsselung via main-Methode getestet").isEqualToIgnoringNewLines(ENCRYPTED_SECRET_MSG);
        }
    }

    private boolean mainMethodWorks(Object[] input, String expected) {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, input), EMPTY_INPUT);
        return result.getOutput().trim().equals(expected);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird bei keinem Argument")
    void testEmptyArgs() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, EMPTY_ARGS), EMPTY_INPUT);
        assertThat(result.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn nur modus gesetzt wurde")
    void testNotTwoArgs() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, TOO_FEW_ARGS), EMPTY_INPUT);
        assertThat(result.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn modus trotz nachricht falsch ist")
    void testWrongModus() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, WRONG_FIRST_ARGS), EMPTY_INPUT);
        assertThat(result.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob eine Fehlermeldung ausgegeben wird, wenn modus stimmt aber nachricht zu lang oder zu kurz ist ist")
    void testWrongNachricht() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, WRONG_SECOND_ARGS), SECRET_TOOLONG_MSG);
        assertThat(result.getOutput()).contains("ERROR");

        var result2 = Program.execute(() -> MAIN_METHOD.invoke(null, WRONG_SECOND_ARGS), SECRET_TOOSHORT_MSG);
        assertThat(result2.getOutput()).contains("ERROR");
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob rasterSpaltenweiseAblesen korrekt funktioniert (alternativ: Teste Verschlüsselung via main-Methode)")
    void testSpaltenweiseLesen() {
        if(methodExists(SPALTENWEISE_LESEN_METHOD) && !mainMethodWorks(INPUT_ENCR, ENCRYPTED_SECRET_MSG)) {
            var result = Program.execute(() -> SPALTENWEISE_LESEN_METHOD.invoke(null, (Object) ROM_ZEILENWEISE_EINGETRAGEN), EMPTY_INPUT);
            assertThat(result.getReturnValue().get()).describedAs("rasterSpaltenweiseAblesen(String) gefunden, Verhalten entspricht aber nicht unserer Spezifikation").isEqualTo(ENCRYPTED_SECRET_MSG_ROM);
        } else {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_ENCR), EMPTY_INPUT);
            assertThat(result.getOutput().trim()).describedAs("rasterSpaltenweiseAblesen(String) nicht gefunden, stattdessen Verschlüsselung via main-Methode getestet").isEqualToIgnoringNewLines(ENCRYPTED_SECRET_MSG);
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob rasterZeilenweiseAblesen korrekt funktioniert (alternativ: Teste Entschlüsselung via main-Methode)")
    void testZeilenweiseLesen() {
        if(methodExists(SPALTENWEISE_LESEN_METHOD) && !mainMethodWorks(INPUT_DECR, ENCRYPTED_SECRET_MSG)) {
            var result = Program.execute(() -> ZEILENWEISE_LESEN_METHOD.invoke(null, (Object) ROM_ZEILENWEISE_EINGETRAGEN), EMPTY_INPUT);
            assertThat(result.getReturnValue().get()).describedAs("rasterZeilenweiseAblesen(String) gefunden, Verhalten entspricht aber nicht unserer Spezifikation").isEqualTo(SECRET_MSG_ROM);
        } else {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_DECR), EMPTY_INPUT);
            assertThat(result.getOutput().trim()).describedAs("rasterZeilenweiseAblesen(String) nicht gefunden, stattdessen Entschlüsselung via main-Methode getestet").isEqualToIgnoringNewLines(SECRET_MSG);
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Verschlüsselung via main-Methode klappt")
    void testEncryptionMain() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_ENCR), EMPTY_INPUT);
        assertThat(result.getOutput().trim()).isEqualToIgnoringNewLines(ENCRYPTED_SECRET_MSG);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob encrypt klappt (alternativ: Teste Verschlüsselung via main-Methode)")
    void testEncryption() {
        if(methodExists(ENCRYPT_METHOD)) {
            var result = Program.execute(() -> ENCRYPT_METHOD.invoke(null, SECRET_MSG_ROM), EMPTY_INPUT);
            assertThat(result.getReturnValue().orElse(null)).describedAs("encrypt(String) gefunden, Verhalten entspricht aber nicht unserer Spezifikation").isEqualTo(ENCRYPTED_SECRET_MSG_ROM);
        } else {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_ENCR_ROM), EMPTY_INPUT);
            assertThat(result.getOutput().trim()).describedAs("encrypt(String) nicht gefunden, stattdessen Verschlüsselung via main-Methode getestet").isEqualToIgnoringNewLines(ENCRYPTED_SECRET_MSG_ROM);
        }
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob Entschlüsselung via main-Methode klappt")
    void testDecryptionMain() {
        var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_DECR), EMPTY_INPUT);
        assertThat(result.getOutput().trim()).isEqualToIgnoringNewLines(SECRET_MSG);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob decrypt klappt (alternativ: Teste Entschlüsselung via main-Methode)")
    void testDecryption() {
        if(methodExists(DECRYPT_METHOD)) {
            var result = Program.execute(() -> DECRYPT_METHOD.invoke(null, ENCRYPTED_SECRET_MSG), EMPTY_INPUT);
            assertThat(result.getReturnValue().orElse(null)).describedAs("decrypt(String) gefunden, Verhalten entspricht aber nicht unserer Spezifikation").isEqualTo(SECRET_MSG);
        } else {
            var result = Program.execute(() -> MAIN_METHOD.invoke(null, INPUT_DECR), EMPTY_INPUT);
            assertThat(result.getOutput().trim()).describedAs("encrypt(String) nicht gefunden, stattdessen Entschlüsselung via main-Methode getestet").isEqualToIgnoringNewLines(SECRET_MSG);
        }
    }
}
