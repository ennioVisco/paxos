package Networking.Runnables;

public interface Consumable<T> {

    void consume(T message);
}
