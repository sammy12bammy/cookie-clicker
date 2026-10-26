package cookieclicker.observer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus implements IGameSubject {

    private final Map<EventType, List<IGameObserver>> observers = new EnumMap<>(EventType.class);

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

    public void postEvent(EventType type, String message) {
        Set<IGameObserver> toNotify = new LinkedHashSet<>();
        toNotify.addAll(observers.getOrDefault(type, List.of()));
        toNotify.addAll(observers.getOrDefault(EventType.ALL, List.of()));

        for (IGameObserver observer : toNotify) {
            observer.update(message);
        }
    }

}
