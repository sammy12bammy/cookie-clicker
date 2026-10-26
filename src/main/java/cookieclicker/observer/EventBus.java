package cookieclicker.observer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus implements IGameSubject {

    private static final EventBus INSTANCE = new EventBus();

    private final Map<EventType, List<IGameObserver>> observers = new EnumMap<>(EventType.class);

    private EventBus() {
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    @Override
    public void attach(IGameObserver observer) {
        attach(observer, List.of(EventType.ALL));
    }

    @Override
    public void attach(IGameObserver observer, List<EventType> events) {
        for (EventType event : events) {
            observers.computeIfAbsent(event, e -> new ArrayList<>()).add(observer);
        }
    }

    @Override
    public void detach(IGameObserver observer) {
        for (List<IGameObserver> attached : observers.values()) {
            attached.remove(observer);
        }
    }

    // Removes every observer. The bus is shared, so tests call this to start from a clean slate.
    public void detachAll() {
        observers.clear();
    }

    public void postEvent(EventType type, String message) {
        Set<IGameObserver> toNotify = new LinkedHashSet<>();
        toNotify.addAll(observers.getOrDefault(type, List.of()));
        toNotify.addAll(observers.getOrDefault(EventType.ALL, List.of()));

        for (IGameObserver observer : toNotify) {
            observer.update(message);
        }
    }

}
