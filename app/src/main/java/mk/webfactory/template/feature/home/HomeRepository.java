/*
 * MIT License
 *
 * Copyright (c) 2020 Web Factory LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
