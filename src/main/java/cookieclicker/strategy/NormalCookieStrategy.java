package cookieclicker.strategy;

import cookieclicker.model.Player;

public class NormalCookieStrategy implements CookieStrategy {

    private static final int DOLLARS = 1;

    @Override
    public void apply(Player player) {
        player.addDollars(DOLLARS);
    }

}
