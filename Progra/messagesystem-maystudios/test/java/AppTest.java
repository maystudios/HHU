import de.hhu.educode.testing.ConstructorCall;
import de.hhu.educode.testing.MethodCall;
import de.hhu.educode.testing.Program;
import de.hhu.educode.testing.RuntimeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private static final RuntimeClass SECURECONNECTION_CLASS = RuntimeClass.forName("SecureConnection");
    private static final RuntimeClass CONNECTION_CLASS = RuntimeClass.forName("Connection");
    private static final RuntimeClass SIMPLEENCRYPTER_CLASS = RuntimeClass.forName("SimpleEncrypter");
    private static final RuntimeClass ENCRYPTER_CLASS = RuntimeClass.forName("Encrypter");
    private static final RuntimeClass STRINGMESSAGE_CLASS = RuntimeClass.forName("StringMessage");
    private static final RuntimeClass MESSAGEAPP_CLASS = RuntimeClass.forName("MessageApp");
    private static final RuntimeClass MESSAGE_CLASS = RuntimeClass.forName("Message");

    private static final MethodCall<Void> MAIN_METHOD = MethodCall.<Void>builder()
            .runtimeClass(MESSAGEAPP_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.STATIC)
            .returnType(void.class)
            .name("main")
            .parameterTypes(new Class<?>[] { String[].class })
            .build();

    private static final ConstructorCall STRINGMESSAGE_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(STRINGMESSAGE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{String.class})
            .build();

    private static final ConstructorCall SIMPLEENCRYPTER_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(SIMPLEENCRYPTER_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{byte.class})
            .build();

    private static final ConstructorCall SECURECONNECTION_CONSTRUCTOR = ConstructorCall.<Void>builder()
            .runtimeClass(SECURECONNECTION_CLASS)
            .modifiers(Modifier.PUBLIC)
            .parameterTypes(new Class<?>[]{ENCRYPTER_CLASS.getActual()})
            .build();

    private static final MethodCall<byte[]> GETCONTENT_METHOD = MethodCall.<byte[]>builder()
            .runtimeClass(STRINGMESSAGE_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(byte[].class)
            .name("getContent")
            .parameterTypes(new Class<?>[] { })
            .build();

    private static final MethodCall<byte[]> CONVERT_METHOD = MethodCall.<byte[]>builder()
            .runtimeClass(SECURECONNECTION_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(byte[].class)
            .name("convert")
            .parameterTypes(new Class<?>[] { MESSAGE_CLASS.getActual() })
            .build();

    private static final MethodCall<Void> SEND_METHOD = MethodCall.<Void>builder()
            .runtimeClass(CONNECTION_CLASS)
            .modifiers(Modifier.PUBLIC | Modifier.FINAL)
            .returnType(void.class)
            .name("send")
            .parameterTypes(new Class<?>[] { MESSAGE_CLASS.getActual() })
            .build();

    private static final MethodCall<Void> ENCRYPT_METHOD = MethodCall.<Void>builder()
            .runtimeClass(SIMPLEENCRYPTER_CLASS)
            .modifiers(Modifier.PUBLIC)
            .returnType(void.class)
            .name("encrypt")
            .parameterTypes(new Class<?>[] { byte[].class })
            .build();

    private static final Object[] ARGS_BLATT = { new String[]{"1", "Der Junge im Eisberg"} };

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob StringMessage das Message-Interface implementiert und öffentlich ist.")
    void testStringMessageInterfaceImplementation() {
        assertThat(STRINGMESSAGE_CLASS.getActual().getInterfaces()[0]).isEqualTo(MESSAGE_CLASS.getActual());
        assertThat(STRINGMESSAGE_CLASS.getActual().getModifiers()).withFailMessage("class »StringMessage« is not public").isEqualTo(Modifier.PUBLIC);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob der Konstruktor von StringMessage mit Parameter \"ABC\" funktioniert und getContent den Inhalt des Strings zurückgibt.")
    void testStringMessageConstructor() {
        var abc = STRINGMESSAGE_CONSTRUCTOR.invoke("ABC");
        STRINGMESSAGE_CONSTRUCTOR.invoke("AAA"); // um sicherzugehen, dass keine Klassenvariable genutzt
        var bytes = Program.execute(() -> GETCONTENT_METHOD.invoke(abc));
        assertThat(bytes.getReturnValue().orElse(new byte[]{})).containsExactly(65, 66, 67);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob SimpleEncrypter das Encrypter-Interface implementiert und öffentlich ist.")
    void testSimpleEncrypterInterfaceImplementation() {
        assertThat(SIMPLEENCRYPTER_CLASS.getActual().getInterfaces()[0]).isEqualTo(ENCRYPTER_CLASS.getActual());
        assertThat(SIMPLEENCRYPTER_CLASS.getActual().getModifiers()).withFailMessage("class »SimpleEncrypter« is not public").isEqualTo(Modifier.PUBLIC);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob der Konstruktor von SimpleEncrypter mit Parameter »2« funktioniert und encrypt das übergebene Array richtig modifiziert.")
    void testSimpleEncrypterConstructor() {
        byte[] bytes = {1, 2, 3};
        var encrypter = SIMPLEENCRYPTER_CONSTRUCTOR.invoke((byte)2);
        SIMPLEENCRYPTER_CONSTRUCTOR.invoke((byte)1); // um sicherzugehen, dass keine Klassenvariable genutzt
        ENCRYPT_METHOD.invoke(encrypter, new Object[]{bytes});
        assertThat(bytes).containsExactly(3, 4, 5);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob SecureConnection von Connection erbt und öffentlich ist.")
    void testSecureConnectionInheritance() {
        assertThat(SECURECONNECTION_CLASS.getActual().getSuperclass()).isEqualTo(CONNECTION_CLASS.getActual());
        assertThat(SECURECONNECTION_CLASS.getActual().getModifiers()).withFailMessage("class »SecureConnection« is not public").isEqualTo(Modifier.PUBLIC);
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob SecureConnection in der convert-Methode Nachrichten korrekt verschlüsselt.")
    void testSecureConnectionEncryption() {
        try {
            // Diese Testvariante funktioniert auch ohne implementierte Message/Encrypter, geht aber von immutable Message-Objekten aus
            Message message = () -> new byte[]{65, 66, 67};
            Encrypter encrypter = (bytes) -> {
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] += 1;
                }
            };
            assertSecureConnectionEncryptsMessage(encrypter, message);
        } catch(AssertionError e) {
            // Es kann sein, dass Message ein privates Array mit den Nachrichten-Daten hält, welches vom Encrypter zurückgegeben wird.
            var encrypter = SIMPLEENCRYPTER_CONSTRUCTOR.invoke((byte)1);
            var message = STRINGMESSAGE_CONSTRUCTOR.invoke("ABC");
            assertSecureConnectionEncryptsMessage(encrypter, message);
        }
    }

    private void assertSecureConnectionEncryptsMessage(Object encrypter, Object message) {
        var secureConnection = SECURECONNECTION_CONSTRUCTOR.invoke(encrypter);
        Encrypter fakeEncrypter = (b) -> {};
        SECURECONNECTION_CONSTRUCTOR.invoke(fakeEncrypter); // um sicherzugehen, dass keine Klassenvariable genutzt
        var bytes = Program.execute(() -> CONVERT_METHOD.invoke(secureConnection, new Object[]{message}));
        assertThat(bytes.getReturnValue().orElse(new byte[]{})).containsExactly(66, 67, 68);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob SecureConnection korrekt verschlüsselte Nachrichten verschickt.")
    void testSecureConnectionSend() {
        try {
            Message message = () -> new byte[]{65, 66, 67};
            Encrypter encrypter = (bytes) -> {
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] += 1;
                }
            };
            var bytes = Program.execute(() -> SEND_METHOD.invoke(SECURECONNECTION_CONSTRUCTOR.invoke(encrypter), new Object[]{message}));
            assertThat(bytes.getOutput().trim()).isEqualTo("66,67,68,");
        } catch(AssertionError e) {
            // Es kann sein, dass Message ein privates Array mit den Nachrichten-Daten hält, welches vom Encrypter zurückgegeben wird.
            var encrypter = SIMPLEENCRYPTER_CONSTRUCTOR.invoke((byte)1);
            var message = STRINGMESSAGE_CONSTRUCTOR.invoke("ABC");
            var bytes = Program.execute(() -> SEND_METHOD.invoke(SECURECONNECTION_CONSTRUCTOR.invoke(encrypter), new Object[]{message}));
            assertThat(bytes.getOutput().trim()).isEqualTo("66,67,68,");
        }
    }

    @Test
    @DisplayName("[2 Punkte] Prüfe, ob MessageApp insgesamt richtig funktioniert und die korrekte Ausgabe macht")
    void testApp() {
        var results = Program.execute(() -> MAIN_METHOD.invoke(null, ARGS_BLATT));
        assertThat(Arrays.stream(results.getOutput().trim().split(",")).mapToInt(Integer::parseInt).toArray()).containsExactly(69,102,115,33,75,118,111,104,102,33,106,110,33,70,106,116,99,102,115,104);
    }

    @Test
    @DisplayName("[1 Punkt] Prüfe, ob vorhandene Instanzvariablen minimale Sichtbarkeit haben")
    void testVisibility() {
        assertThat(Arrays.stream(SECURECONNECTION_CLASS.getActual().getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers()))).withFailMessage("Instanzvariablen in SecureConnection haben keine minimale Sichtbarkeit").hasSize(0);
        assertThat(Arrays.stream(SIMPLEENCRYPTER_CLASS.getActual().getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers()))).withFailMessage("Instanzvariablen in SimpleEncrypter haben keine minimale Sichtbarkeit").hasSize(0);
        assertThat(Arrays.stream(STRINGMESSAGE_CLASS.getActual().getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers()))).withFailMessage("Instanzvariablen in StringMessage haben keine minimale Sichtbarkeit").hasSize(0);
        // Testen wir anders
//        assertThat(Arrays.stream(SECURECONNECTION_CLASS.getActual().getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers()) && Modifier.isPrivate(f.getModifiers()))).withFailMessage("SecureConnection hat keine privaten Instanzvariablen").isNotEmpty();
//        assertThat(Arrays.stream(SIMPLEENCRYPTER_CLASS.getActual().getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers()) && Modifier.isPrivate(f.getModifiers()))).withFailMessage("SecureConnection hat keine privaten Instanzvariablen").isNotEmpty();
//        assertThat(Arrays.stream(STRINGMESSAGE_CLASS.getActual().getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers()) && Modifier.isPrivate(f.getModifiers()))).withFailMessage("SecureConnection hat keine privaten Instanzvariablen").isNotEmpty();
    }

}
