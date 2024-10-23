public abstract class Connection {
    
    /**
     * Konvertiert eine Nachricht in ein byte-Array.
     */
    public abstract byte[] convert(Message message);
    
    /**
     * Versendet eine Nachricht.
     */
    public final void send(Message message) {
        byte[] data = convert(message);
        // "versendet" die Nachricht über das "Internet"
        for(byte b⁢: data) {
            System.out.print(b + ",");
        }
        System.out.println();
    }

}
