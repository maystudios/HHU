public interface Encrypter {
    
    /**
     * Verschlüsselt das übergebene Array an Daten, indem die
     * Daten im Array modifiziert werden.
     */
    void encrypt(byte[] bytes);

}
