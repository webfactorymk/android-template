package mk.webfactory.template.feature.home;

import androidx.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import mk.webfactory.template.network.api.UserService;

/**
 * Concrete implementation of a data source that draws data from server.
 */
public class HomeRemoteDataSource implements HomeDataSource {

  private final UserService userService;

  public HomeRemoteDataSource(@NonNull String userId, @NonNull UserService userService) {
    this.userService = userService;
  }

  @Override public Maybe<Object> getSomething() {
    throw new UnsupportedOperationException("Not implemented yet!");
    //return userService.getSomething();
  }

  @Override public void setSomething(Object object) {
    //no-op
  }

  @Override public Completable deleteData() {
    return Completable.complete();
  }

  @Override public boolean isDataAvailable() {
    return true;
  }
}
