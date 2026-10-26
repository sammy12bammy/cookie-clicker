package cookieclicker.model;

public class Player {

    private static final int BOOST_MULTIPLIER = 2;

    private int dollars;
    private int boostedClicksRemaining;
    private int currentClickMultiplier = 1;

    public int getDollars() {
        return dollars;
    }

    public void addDollars(int amount) {
        dollars += amount * currentClickMultiplier;
    }

    public void subtractDollars(int amount) {
        dollars = Math.max(0, dollars - amount);
    }

    public void beginClick() {
        if (boostedClicksRemaining > 0) {
            boostedClicksRemaining--;
            currentClickMultiplier = BOOST_MULTIPLIER;
        } else {
            currentClickMultiplier = 1;
        }
    }

    public void activateDoubleDollars(int clicks) {
        boostedClicksRemaining = clicks;
    }

    public int getBoostedClicksRemaining() {
        return boostedClicksRemaining;
    }

}
