package mk.webfactory.template.feature.home;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import mk.webfactory.template.data.storage.InMemoryStorage;

/**
 * Concrete implementation of a data source as in memory cache.
 */
public class HomeLocalDataSource implements HomeDataSource {

  private final String userId;
  private final InMemoryStorage<Object> storage;
  private boolean isCacheCorrupt;                 //implement any cache control here

  public HomeLocalDataSource(String userId, InMemoryStorage<Object> storage) {
    this.userId = userId;
    this.storage = storage;
    this.isCacheCorrupt = true;
  }

  @Override public Maybe<Object> getSomething() {
    if (!isCacheCorrupt) {
      return storage.retrieve().firstElement();
    } else {
      return Maybe.empty();
    }
  }

  @Override public void setSomething(Object object) {
//    storage.save(object).subscribeBy();
    isCacheCorrupt = false;
  }

  @Override public Completable deleteData() {
    isCacheCorrupt = true;
    return storage.delete();
  }

  @Override public boolean isDataAvailable() {
    return !isCacheCorrupt;
  }
}
