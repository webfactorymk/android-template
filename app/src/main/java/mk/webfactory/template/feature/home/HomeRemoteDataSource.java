package mk.webfactory.template.feature.home;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import mk.webfactory.template.network.api.UserService;

import static mk.webfactory.template.util.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source that draws data from server.
 */
public class HomeRemoteDataSource implements HomeDataSource {

  private final String userId;
  private final UserService userService;

  public HomeRemoteDataSource(String userId, UserService userService) {
    this.userId = checkNotNull(userId);
    this.userService = checkNotNull(userService);
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

  @Override public String getUserId() {
    return userId;
  }
}
