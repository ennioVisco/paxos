package Execution.Runnables;

import Messages.Message;

public interface Consumable {

    void consume(Message message);
}
