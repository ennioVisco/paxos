package Messages;

import java.io.Serializable;
import java.util.UUID;

public interface Message extends Serializable {

    UUID getSender();

    UUID getRecipient();

    void setRecipient(UUID recipient);
}
