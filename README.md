# Cookie Clicker

Lizze and Sam's OOP project.

    Names: Lizze <last name>, Sam Balent
    Java Version: 25
    Build Tool: Gradle (wrapper included, 9.7.1)
    Test Coverage Screenshot: <paste here>

> **Status: template.** Every section marked `TBD` or listed under *Assumptions* is a
> placeholder to confirm or replace as the design firms up.

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

Candidates. Confirm, drop or replace, and fill in the details.

| Pattern | Where | How / why |
|---|---|---|
| Observer | `observer/`, game state to UI | The UI and any stats or log listeners subscribe to game events (cookies changed, upgrade bought) so the model never depends on the UI. |
| Strategy | `strategy/` | Interchangeable production or cost-scaling rules per generator (linear, exponential). Swapped without touching the generator class. |
| Factory | `factory/` | Builds generators and upgrades from definitions, so the engine codes to the `Generator` / `Upgrade` abstractions. `TBD` Factory vs SPI. |
| Decorator | `model/` (optional) | Upgrades wrap a generator to boost its output. `TBD` |
| Command | `engine/` (optional) | Click and purchase actions as objects, for undo or replay. `TBD` |

## Architecture / Project Structure

```
src/main/java/cookieclicker/
  model/      Core domain: Cookie wallet, Generator, Upgrade (interfaces + implementations)
  engine/     Game loop, tick handling, wiring of the pieces (dependency injection happens here)
  strategy/   Production and cost-scaling strategies
  observer/   Event/listener interfaces and subjects
  factory/    Creation of generators and upgrades
  ui/         Swing views, only aware of observer/model interfaces
  util/       Shared helpers (clock, formatting)
src/main/resources/logback.xml
src/test/java/cookieclicker/    Mirrors the main packages
src/test/resources/features/    Cucumber feature files
docs/                           Design notes and diagrams
```

## OO Principles (rubric)

* **Coding to abstractions:** `TBD` list the main interfaces.
* **Polymorphism:** `TBD` e.g. different `Generator` implementations driven through one interface.
* **Dependency injection:** `TBD` e.g. the engine receives its clock, strategies and listeners through its constructor.

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

## Tests (rubric: at least 5 meaningful cases)

`TBD`: list the test cases and what each one verifies.

## Team Notes

`TBD`
