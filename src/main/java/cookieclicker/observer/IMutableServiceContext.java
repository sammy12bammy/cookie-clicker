package cookieclicker.observer;

public interface IMutableServiceContext extends IServiceContext {

    <T> void register(Class<T> capability, T instance);

}
