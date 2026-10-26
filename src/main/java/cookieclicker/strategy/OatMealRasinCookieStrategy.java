package cookieclicker.strategy;

import cookieclicker.model.Player;

public class OatMealRasinCookieStrategy implements CookieStrategy {

    private static final int DOLLARS = 5;

    @Override
    public void apply(Player player) {
        player.subtractDollars(DOLLARS);
    }

}
