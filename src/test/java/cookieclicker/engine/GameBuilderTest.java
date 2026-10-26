package cookieclicker.engine;

import cookieclicker.model.Cookie;
import cookieclicker.model.Player;
import cookieclicker.observer.EventBus;
import cookieclicker.observer.IGameObserver;
import cookieclicker.strategy.NormalCookieStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameBuilderTest {

    @BeforeEach
    @AfterEach
    void resetBus() {
        EventBus.getInstance().detachAll();
    }

    @Test
    void buildWithNoOptionsGivesPlainGameWithFreshPlayer() {
        Game game = new Game.GameBuilder().build();

        assertFalse(game instanceof ObservableGame);
        assertEquals(0, game.getPlayer().getDollars());
        assertEquals(0, game.getCookies().size());
    }

    @Test
    void suppliedPlayerIsUsed() {
        Player player = new Player();
        Game game = new Game.GameBuilder().withPlayer(player).build();

        assertSame(player, game.getPlayer());
    }

    @Test
    void defaultCookiesAreAllFourKinds() {
        Game game = new Game.GameBuilder().withDefaultCookies().build();

        assertEquals(List.of("Normal", "Chocolate Chip", "Oatmeal Raisin", "Sugar"),
                game.getCookies().stream().map(Cookie::getName).toList());
    }

    @Test
    void builtGameCookiesCannotBeModified() {
        Game game = new Game.GameBuilder().withDefaultCookies().build();

        assertThrows(UnsupportedOperationException.class,
                () -> game.getCookies().add(new Cookie("Extra", new NormalCookieStrategy())));
    }

    @Test
    void addingAnObserverMakesAnObservableGameThatNotifiesIt() {
        List<String> messages = new ArrayList<>();
        IGameObserver recorder = messages::add;
        Game game = new Game.GameBuilder().withObserver(recorder).build();

        assertInstanceOf(ObservableGame.class, game);
        game.clickCookie(new Cookie("Normal", new NormalCookieStrategy()));
        assertEquals(List.of("Clicked Normal cookie", "Dollars: 1"), messages);
    }

    @Test
    void suppliedEventBusIsTheOneUsed() {
        EventBus bus = EventBus.getInstance();
        List<String> messages = new ArrayList<>();
        bus.attach(messages::add);

        new Game.GameBuilder().withEventBus(bus).build()
                .clickCookie(new Cookie("Normal", new NormalCookieStrategy()));

        assertEquals(2, messages.size());
    }

}
