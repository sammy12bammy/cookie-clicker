package cookieclicker.observer;

public interface IGameObserverProvider {

    IGameObserver create(IServiceContext context);

}
