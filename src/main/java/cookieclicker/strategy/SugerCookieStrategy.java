package cookieclicker.strategy;

import cookieclicker.model.Player;

public class SugerCookieStrategy implements CookieStrategy {

    private static final int BOOSTED_CLICKS = 5;

    @Override
    public void apply(Player player) {
        player.activateDoubleDollars(BOOSTED_CLICKS);
    }

}
