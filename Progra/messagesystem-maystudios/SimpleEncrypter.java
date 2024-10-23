/**
 * The SimpleEncrypter class implements the Encrypter interface and provides a
 * simple encryption mechanism.
 * It encrypts byte arrays by adding a key value to each byte.
 */
public class SimpleEncrypter implements Encrypter {

    private byte key;

    /**
     * Constructs a SimpleEncrypter object with the specified key.
     *
     * @param key the key value used for encryption
     */
    public SimpleEncrypter(byte key) {
        this.key = key;
    }

    /**
     * Encrypts the given byte array by adding the key value to each byte.
     *
     * @param bytes the byte array to be encrypted
     */
    @Override
    public void encrypt(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] + key);
        }
    }

}
