package mk.webfactory.template.feature.home;

import androidx.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;
import mk.webfactory.template.di.qualifier.Local;
import mk.webfactory.template.di.qualifier.Remote;
import mk.webfactory.template.di.scope.UserScope;
import timber.log.Timber;


/**
 * Concrete implementation to load data from multiple sources
 * <p>
 * This implements a synchronisation between cached data and data obtained from server,
 * by using the remote data source only if the cache doesn't exist, is empty or expired.
 */
@UserScope
public final class HomeRepository implements HomeDataSource {

  private final HomeDataSource homeRemoteDataSource;
  private final HomeDataSource homeLocalDataSource;

  @Inject public HomeRepository(
      @NonNull @Remote HomeDataSource homeRemoteDataSource,
      @NonNull @Local HomeDataSource homeLocalDataSource) {
    this.homeRemoteDataSource = homeRemoteDataSource;
    this.homeLocalDataSource = homeLocalDataSource;
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

  @Override public Completable deleteData() {
    return deleteLocalData();
  }

  public Completable deleteLocalData() {
    return homeLocalDataSource.deleteData();
  }
}
