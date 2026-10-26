package cookieclicker.observer;

import java.util.Optional;

public interface IServiceContext {

    <T> Optional<T> get(Class<T> type);

}
