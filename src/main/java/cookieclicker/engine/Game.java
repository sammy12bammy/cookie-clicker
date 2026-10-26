package cookieclicker.engine;

import cookieclicker.model.Cookie;
import cookieclicker.model.Player;
import cookieclicker.observer.EventBus;
import cookieclicker.observer.IGameObserver;
import cookieclicker.strategy.ChocolateChipCookieStrategy;
import cookieclicker.strategy.NormalCookieStrategy;
import cookieclicker.strategy.OatMealRasinCookieStrategy;
import cookieclicker.strategy.CookieStrategy;
import cookieclicker.strategy.SugerCookieStrategy;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Player player;
    private final List<Cookie> cookies;

    public Game(Player player) {
        this(player, List.of());
    }

    public Game(Player player, List<Cookie> cookies) {
        this.player = player;
        this.cookies = List.copyOf(cookies);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void clickCookie(Cookie cookie) {
        cookie.doAction(player);
    }

    public void changeStrategy(Cookie cookie, CookieStrategy strategy) {
        cookie.setStrategy(strategy);
    }

    public static class GameBuilder {

        private Player player;
        private final List<Cookie> cookies = new ArrayList<>();
        private final List<IGameObserver> observers = new ArrayList<>();
        private EventBus eventBus;
        private boolean observable;
        private boolean observersFoundViaSPI;

        public GameBuilder withPlayer(Player player) {
            this.player = player;
            return this;
        }

        public GameBuilder withCookie(Cookie cookie) {
            cookies.add(cookie);
            return this;
        }

        public GameBuilder withDefaultCookies() {
            return withCookie(new Cookie("Normal", new NormalCookieStrategy()))
                    .withCookie(new Cookie("Chocolate Chip", new ChocolateChipCookieStrategy()))
                    .withCookie(new Cookie("Oatmeal Raisin", new OatMealRasinCookieStrategy()))
                    .withCookie(new Cookie("Sugar", new SugerCookieStrategy()));
        }

        public GameBuilder observable() {
            this.observable = true;
            return this;
        }

        public GameBuilder withEventBus(EventBus eventBus) {
            this.eventBus = eventBus;
            return observable();
        }

        public GameBuilder withObserver(IGameObserver observer) {
            observers.add(observer);
            return observable();
        }

        public GameBuilder withObserversFoundViaSPI() {
            this.observersFoundViaSPI = true;
            return observable();
        }

        public Game build() {
            Player gamePlayer = (player != null) ? player : new Player();
            if (!observable) {
                return new Game(gamePlayer, cookies);
            }

            EventBus bus = (eventBus != null) ? eventBus : EventBus.getInstance();
            ObservableGame game = new ObservableGame(gamePlayer, bus, cookies);
            observers.forEach(game::attach);
            if (observersFoundViaSPI) {
                game.attachObserversFoundViaSPI();
            }
            return game;
        }

    }

}
