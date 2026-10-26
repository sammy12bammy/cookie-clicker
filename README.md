# Cookie Clicker

    Names: Lizze Ruff, Sam Balent
    Java Version: 25
    Build Tool: Gradle (wrapper included, 9.7.1)
    Test Coverage Screenshot: <paste here>

## Overview

A Cookie Clicker style incremental game: the player clicks to earn cookies, spends them on
generators (cursors, grandmas, farms, ...) and upgrades, and watches production compound over time.

## Assumptions

These are guesses made while setting up the project. Change any that are wrong.

1. **Java 25** with a Gradle toolchain, matching the earlier homework (`hw8-stack-attack`).
2. **Swing** for the UI (no extra dependency), kept behind interfaces so the game logic can run headless in tests.
3. **No persistence** in the first iteration. Saving and loading is a stretch goal.
4. **Time is injected** (a tick source or clock abstraction), so production can be tested without waiting.
5. **Currency is whole or fractional cookies**: `TBD` (`double` vs `BigDecimal`).
6. **Balance numbers** (costs, growth rates) are placeholders and live in one config location, not scattered through classes.
7. **Logging** uses SLF4J/Logback, as in earlier homeworks.
8. Libraries carried over from Polymorphia (`GameObserverSPI`, `freetts`) are present but **commented out** in `build.gradle`. They are only needed if we reuse the observer SPI or add speech.

## Design Patterns (rubric: at least 3, with where and how)

Five patterns are in the code today.

| Pattern | Where | Role |
|---|---|---|
| Strategy | `strategy/`, used by `model/Cookie` | What a click does |
| Observer | `observer/`, fired from `engine/ObservableGame` | How the UI learns about changes |
| Singleton | `observer/EventBus` | One shared event bus |
| Builder | `engine/Game.GameBuilder` | Assembling a configured game |
| Factory Method via SPI | `observer/IGameObserverProvider` | Plugging in the UI without naming it |

### Strategy

* **Where:** `CookieStrategy` is the interface, with `NormalCookieStrategy`, `ChocolateChipCookieStrategy`, `OatMealRasinCookieStrategy` and `SugerCookieStrategy`. `Cookie` holds one.
* **How:** `Cookie.doAction(player)` calls `strategy.apply(player)` and knows nothing about the concrete class. Normal adds $1, Chocolate Chip adds $5, Oatmeal subtracts $5, and Sugar doubles earnings for the next 5 clicks.
* **Why:** a new kind of cookie is a new strategy class, with no change to `Cookie`. The strategy is passed in through the constructor, and `Cookie.setStrategy` swaps it while the game runs.

### Observer

* **Where:** `IGameObserver` (observer) and `IGameSubject` (subject), with `EventBus` keeping the observer lists. `ObservableGame` extends `Game` and posts the events.
* **How:** after a click, `ObservableGame` posts `COOKIE_CLICKED`, `DOLLARS_CHANGED` and `BOOST_CHANGED`. A strategy swap posts `STRATEGY_CHANGED`. Observers can subscribe to chosen event types or to `ALL`. They receive a status message and read the current state through the read-only `IPlayerView`.
* **Why:** the game logic never imports the UI. A Swing view, a logger or a test recorder all attach the same way. The design follows the observer setup from homework 8.

### Singleton

* **Where:** `EventBus.getInstance()`, with a private constructor.
* **How:** every game and observer talks to the same bus, so an observer attached anywhere hears events from any `ObservableGame`.
* **Trade-off:** a shared instance carries state between tests, so tests call `EventBus.getInstance().detachAll()` before and after each run. `ObservableGame` also accepts a bus through its constructor, so it still depends only on the instance it was given.

### Builder

* **Where:** `Game.GameBuilder`.
* **How:** `new Game.GameBuilder().withDefaultCookies().withObserver(o).build()`. Every `with...` call is optional. If nothing observer-related is requested, `build()` returns a plain `Game`. Otherwise it returns an `ObservableGame` wired to the event bus and any observers.
* **Why:** a game has several independent options (player, cookies, bus, observers, SPI discovery). A builder avoids a pile of constructor overloads, and callers only see the `Game` type.

### Factory Method via SPI

* **Where:** `IGameObserverProvider.create(IServiceContext)`, discovered by `ObservableGame.attachObserversFoundViaSPI()` with `ServiceLoader`.
* **How:** the UI module will implement `IGameObserverProvider` and list it in `META-INF/services/cookieclicker.observer.IGameObserverProvider`. The game hands each provider an `IServiceContext` containing the `IPlayerSubject` and `IGameSubject`. The provider builds its observer from that.
* **Why:** the engine creates observers without referencing a concrete UI class, so the UI can be added or removed without editing the engine.

## Libraries

| Library | Purpose |
|---|---|
| `logback-classic` | Logging |
| JUnit 5 (`jupiter`, `platform-suite`) | Unit tests |
| Mockito | Fake collaborators when testing injected dependencies |
| Cucumber (`java`, `picocontainer`, `junit-platform-engine`) | BDD tests |

## Build and Run

```bash
./gradlew build     # compile and run tests
./gradlew test      # tests only
./gradlew run       # needs cookieclicker.CookieClicker to exist
```

## Team Notes

`TBD`
