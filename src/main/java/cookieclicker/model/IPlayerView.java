package cookieclicker.model;

// Read-only window onto the player, handed to observers so they cannot change game state.
public interface IPlayerView {

    int getDollars();

    int getBoostedClicksRemaining();

}
