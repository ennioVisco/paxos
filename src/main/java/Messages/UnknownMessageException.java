package Messages;

public class UnknownMessageException extends Exception {
    public UnknownMessageException(Message message) {
        super(message.toString());
    }
}
