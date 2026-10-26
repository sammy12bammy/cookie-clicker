package cookieclicker.engine;

import cookieclicker.model.Cookie;
import cookieclicker.model.IPlayerView;
import cookieclicker.model.Player;
import cookieclicker.observer.EventBus;
import cookieclicker.observer.EventType;
import cookieclicker.observer.IGameObserver;
import cookieclicker.observer.IGameObserverProvider;
import cookieclicker.observer.IGameSubject;
import cookieclicker.observer.IMutableServiceContext;
import cookieclicker.observer.IPlayerSubject;
import cookieclicker.observer.SimpleServiceContext;
import cookieclicker.strategy.CookieStrategy;

import java.util.List;
import java.util.ServiceLoader;

public class ObservableGame extends Game implements IGameSubject, IPlayerSubject {

    private final EventBus eventBus;

    public ObservableGame(Player player, EventBus eventBus) {
        super(player);
        this.eventBus = eventBus;
    }

    public ObservableGame(Player player) {
        this(player, new EventBus());
    }

    @Override
    public void attach(IGameObserver observer) {
        eventBus.attach(observer);
    }

    @Override
    public void attach(IGameObserver observer, List<EventType> events) {
        eventBus.attach(observer, events);
    }

    @Override
    public void detach(IGameObserver observer) {
        eventBus.detach(observer);
    }

    @Override
    public IPlayerView getPlayerView() {
        return getPlayer();
    }

    @Override
    public void clickCookie(Cookie cookie) {
        int dollarsBefore = getPlayer().getDollars();
        int boostBefore = getPlayer().getBoostedClicksRemaining();

        super.clickCookie(cookie);

        eventBus.postEvent(EventType.COOKIE_CLICKED, "Clicked " + cookie.getName() + " cookie");
        if (getPlayer().getDollars() != dollarsBefore) {
            eventBus.postEvent(EventType.DOLLARS_CHANGED, "Dollars: " + getPlayer().getDollars());
        }
        if (getPlayer().getBoostedClicksRemaining() != boostBefore) {
            eventBus.postEvent(EventType.BOOST_CHANGED,
                    "Double dollars for " + getPlayer().getBoostedClicksRemaining() + " more clicks");
        }
    }

    @Override
    public void changeStrategy(Cookie cookie, CookieStrategy strategy) {
        super.changeStrategy(cookie, strategy);
        eventBus.postEvent(EventType.STRATEGY_CHANGED,
                cookie.getName() + " cookie now uses " + strategy.getClass().getSimpleName());
    }

    public void attachObserversFoundViaSPI() {
        IMutableServiceContext context = new SimpleServiceContext();
        context.register(IPlayerSubject.class, this);
        context.register(IGameSubject.class, this);
        for (IGameObserverProvider provider : ServiceLoader.load(IGameObserverProvider.class)) {
            attach(provider.create(context));
        }
    }

}
