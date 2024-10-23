public class MessageApp {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            throw new Exception("Only two Arguments are allowed!");
        }

        try {
            byte key = Byte.parseByte(args[0]);
            String content = args[1];

            Message message = new StringMessage(content);
            Encrypter encrypter = new SimpleEncrypter(key);
            Connection secure = new SecureConnection(encrypter);
            secure.send(message);
        } catch (Exception err) {
            throw err;
        }

    }
}
