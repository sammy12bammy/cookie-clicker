package cookieclicker.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleServiceContext implements IMutableServiceContext {

    private final Map<Class<?>, Object> services = new HashMap<>();

    @Override
    public <T> void register(Class<T> capability, T instance) {
        if (!capability.isInstance(instance)) {
            throw new IllegalArgumentException(
                    instance.getClass().getName() + " does not implement " + capability.getName());
        }
        if (services.containsKey(capability)) {
            throw new IllegalStateException("Service already registered: " + capability.getName());
        }
        services.put(capability, instance);
    }

    @Override
    public <T> Optional<T> get(Class<T> capability) {
        return Optional.ofNullable(capability.cast(services.get(capability)));
    }

}
