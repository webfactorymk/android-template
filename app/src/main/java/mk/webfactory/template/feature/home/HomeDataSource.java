package mk.webfactory.template.feature.home;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface HomeDataSource {

  Maybe<Object> getSomething();

  void setSomething(Object object);

  Completable deleteData();

  boolean isDataAvailable();
}
