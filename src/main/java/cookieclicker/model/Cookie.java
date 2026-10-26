package cookieclicker.model;

import cookieclicker.strategy.CookieStrategy;

import java.util.Objects;

public class Cookie {

    private final String name;
    private CookieStrategy strategy;

    public Cookie(String name, CookieStrategy strategy) {
        this.name = name;
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public String getName() {
        return name;
    }

    public void setStrategy(CookieStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public void doAction(Player player) {
        player.beginClick();
        strategy.apply(player);
    }

}
