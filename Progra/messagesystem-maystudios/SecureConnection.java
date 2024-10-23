/**
 * This class represents a secure connection that extends the Connection class.
 * It encrypts the message content using an Encrypter object before converting
 * it to bytes.
 */
public class SecureConnection extends Connection {

    private Encrypter encrypter;

    /**
     * Constructs a SecureConnection object with the specified Encrypter.
     * 
     * @param encrypter the Encrypter object used for encryption
     */
    public SecureConnection(Encrypter encrypter) {
        this.encrypter = encrypter;
    }

    /**
     * Converts the message content to bytes after encrypting it using the Encrypter
     * object.
     * 
     * @param message the message to be converted
     * @return the encrypted message content as bytes
     */
    @Override
    public byte[] convert(Message message) {
        byte[] messageAsByte = message.getContent();
        encrypter.encrypt(messageAsByte);
        return messageAsByte;
    }

}
