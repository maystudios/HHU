/**
 * Represents a message that contains a string content.
 */
public class StringMessage implements Message {

    private String messageContent = "";

    /**
     * Constructs a StringMessage object with the specified message content.
     * 
     * @param messageContent the content of the message
     */
    public StringMessage(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * Returns the content of the message as a byte array.
     *
     * @return the content of the message as a byte array
     */
    @Override
    public byte[] getContent() {
        return messageContent.getBytes();
    }
}
