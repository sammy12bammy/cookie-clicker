package cookieclicker.strategy;

import cookieclicker.model.Player;

public class ChocolateChipCookieStrategy implements CookieStrategy {

    private static final int DOLLARS = 5;

    @Override
    public void apply(Player player) {
        player.addDollars(DOLLARS);
    }

}
