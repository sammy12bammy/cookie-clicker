package cookieclicker.observer;

import java.util.List;

public interface IGameSubject {

    void attach(IGameObserver observer);

    default void attach(IGameObserver observer, List<EventType> events) {
        attach(observer);
    }

    default void detach(IGameObserver observer) {
        // todo polly
    }

}
