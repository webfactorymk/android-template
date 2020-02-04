package mk.webfactory.template.data.storage;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class StorageHelpers {

  public static <T> Function<T, Observable<T>> deferSaveIn(final Storage<T> storage) {
    return deferSaveIn(storage, false);
  }

  /**
   * Unlike {@linkplain #deferSaveIn}, the create observable will not be allowed to throw,
   * and instead the object being saved will be returned when an error occurs.
   */
  public static <T> Function<T, Observable<T>> deferSaveInAndIgnoreErrors(final Storage<T> storage) {
    return deferSaveIn(storage, true);
  }

  private static <T> Function<T, Observable<T>> deferSaveIn(final Storage<T> storage,
      final boolean continueOnError) {
    return new Function<T, Observable<T>>() {
      @Override public Observable<T> apply(@NonNull final T t) throws Exception {
        if (continueOnError) {
          return storage.save(t).onErrorReturn(new Function<Throwable, T>() {
            @Override public T apply(@NonNull Throwable throwable) throws Exception {
              return t;
            }
          });
        }
        return storage.save(t);
      }
    };
  }

  private StorageHelpers() {
  }
}
