package Messages;

import java.util.UUID;

public interface Message {

    UUID getSender();

    UUID getRecipient();

    void setRecipient(UUID recipient);
}
