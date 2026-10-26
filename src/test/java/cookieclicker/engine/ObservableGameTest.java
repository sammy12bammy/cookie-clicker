package cookieclicker.engine;

import cookieclicker.model.Cookie;
import cookieclicker.model.Player;
import cookieclicker.observer.EventBus;
import cookieclicker.observer.EventType;
import cookieclicker.observer.IGameObserver;
import cookieclicker.strategy.ChocolateChipCookieStrategy;
import cookieclicker.strategy.NormalCookieStrategy;
import cookieclicker.strategy.SugerCookieStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObservableGameTest {
    final int DOLLARS_AFTER_CHOCOLATE_CHIP = 5;

    private ObservableGame game;
    private List<String> messages;
    private IGameObserver recorder;

    @BeforeEach
    void setUp() {
        EventBus.getInstance().detachAll();
        game = new ObservableGame(new Player());
        messages = new ArrayList<>();
        recorder = messages::add;
    }

    @AfterEach
    void tearDown() {
        EventBus.getInstance().detachAll();
    }

    @Test
    void eventBusIsASingleton() {
        assertSame(EventBus.getInstance(), EventBus.getInstance());
    }

    @Test
    void gamesShareTheSameBus() {
        game.attach(recorder);
        new ObservableGame(new Player()).clickCookie(new Cookie("Normal", new NormalCookieStrategy()));

        assertEquals(List.of("Clicked Normal cookie", "Dollars: 1"), messages);
    }

    @Test
    void attachedObserverHearsAboutClicksAndDollarChanges() {
        game.attach(recorder);
        game.clickCookie(new Cookie("Normal", new NormalCookieStrategy()));

        assertEquals(List.of("Clicked Normal cookie", "Dollars: 1"), messages);
    }

    @Test
    void observerOnlyReceivesEventTypesItAskedFor() {
        game.attach(recorder, List.of(EventType.DOLLARS_CHANGED));
        game.clickCookie(new Cookie("Chip", new ChocolateChipCookieStrategy()));

        assertEquals(List.of("Dollars: "+ DOLLARS_AFTER_CHOCOLATE_CHIP), messages);
    }

    @Test
    void detachedObserverHearsNothing() {
        game.attach(recorder);
        game.detach(recorder);
        game.clickCookie(new Cookie("Normal", new NormalCookieStrategy()));

        assertTrue(messages.isEmpty());
    }

    @Test
    void observerSubscribedToTypeAndAllIsNotifiedOnce() {
        game.attach(recorder);
        game.attach(recorder, List.of(EventType.COOKIE_CLICKED));
        game.clickCookie(new Cookie("Sugar", new SugerCookieStrategy()));

        // only started with one click, pretty self-explanatory
        assertEquals(1, messages.stream().filter(m -> m.startsWith("Clicked")).count());
    }

    @Test
    void sugarCookieAnnouncesBoostAndFollowingClicksCountItDown(){
        Cookie normal = new Cookie("Normal", new NormalCookieStrategy());
        game.clickCookie(new Cookie("Sugar", new SugerCookieStrategy()));
        game.attach(recorder, List.of(EventType.BOOST_CHANGED));
        game.clickCookie(normal);

        assertEquals(List.of("Double dollars for 4 more clicks"), messages);
        final int DOLLARS_AFTER_SUGAR = 2;
        assertEquals(DOLLARS_AFTER_SUGAR, game.getPlayerView().getDollars());
    }

    @Test
    void changingStrategyIsAnnouncedAndTakesEffect(){
        Cookie cookie = new Cookie("Shape-shifter", new NormalCookieStrategy());
        game.attach(recorder, List.of(EventType.STRATEGY_CHANGED));
        game.changeStrategy(cookie, new ChocolateChipCookieStrategy());
        game.clickCookie(cookie);

        assertEquals(List.of("Shape-shifter cookie now uses ChocolateChipCookieStrategy"), messages);
        assertEquals(DOLLARS_AFTER_CHOCOLATE_CHIP, game.getPlayerView().getDollars());
    }

}
