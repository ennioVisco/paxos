package Messages;

public interface Message {

    Integer getSender();

    Integer getRecipient();

    void setRecipient(Integer recipient);
}
