package cookieclicker.engine;

import cookieclicker.model.Cookie;
import cookieclicker.model.Player;
import cookieclicker.strategy.CookieStrategy;

public class Game {

    private final Player player;

    public Game(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void clickCookie(Cookie cookie) {
        cookie.doAction(player);
    }

    public void changeStrategy(Cookie cookie, CookieStrategy strategy) {
        cookie.setStrategy(strategy);
    }

}
