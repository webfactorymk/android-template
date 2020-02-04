package mk.webfactory.template.feature.home;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;
import javax.inject.Named;
import mk.webfactory.template.di.qualifier.Local;
import mk.webfactory.template.di.qualifier.Remote;
import mk.webfactory.template.di.scope.UserScope;
import timber.log.Timber;

import static mk.webfactory.template.util.Preconditions.checkArgument;
import static mk.webfactory.template.util.Preconditions.checkNotNull;

/**
 * Concrete implementation to load data from multiple sources
 * <p>
 * This implements a synchronisation between cached data and data obtained from server,
 * by using the remote data source only if the cache doesn't exist, is empty or expired.
 */
@UserScope
public final class HomeRepository implements HomeDataSource {

  private final String userId;
  private final HomeDataSource homeRemoteDataSource;
  private final HomeDataSource homeLocalDataSource;

  @Inject public HomeRepository(
      @Remote HomeDataSource homeRemoteDataSource,
      @Local HomeDataSource homeLocalDataSource) {
    this.homeRemoteDataSource = checkNotNull(homeRemoteDataSource);
    this.homeLocalDataSource = checkNotNull(homeLocalDataSource);
  }

  @Override public Maybe<Object> getSomething() {
    if (homeLocalDataSource.isDataAvailable()) {
      Timber.d("HomeRepository: Local data fetched");
      return homeLocalDataSource.getSomething();
    }
    if (homeRemoteDataSource.isDataAvailable()) {
      Timber.d("HomeRepository: Remote data fetched");
      return homeRemoteDataSource.getSomething()
          .doOnSuccess(new Consumer<Object>() {
            @Override public void accept(Object item) throws Exception {
              homeLocalDataSource.setSomething(item);
            }
          });
    }
    return Maybe.error(new Exception("No data available"));
  }

  @Override public void setSomething(Object object) {
    homeRemoteDataSource.setSomething(object);
    homeLocalDataSource.setSomething(object);
  }

  @Override public boolean isDataAvailable() {
    return homeRemoteDataSource.isDataAvailable() || homeLocalDataSource.isDataAvailable();
  }

  @Override public String getUserId() {
    return userId;
  }

  @Override public Completable deleteData() {
    return deleteLocalData();
  }

  public Completable deleteLocalData() {
    return homeLocalDataSource.deleteData();
  }
}
