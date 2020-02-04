package mk.webfactory.template.data.storage;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Generic single object store.
 * <p>Note that the storage may cache values for any subsequent queries.
 */
public interface Storage<T> {

  /**
   * @return the saved data.
   * @throws StorageException
   * @see #isLocal()
   */
  Observable<T> save(T t);

  /** @throws StorageException */
  Observable<T> retrieve();

  /**
   * @return completes or calls onError if an error occurs
   * @throws StorageException
   */
  Completable delete();

  /** Test whether the storage is local or remote. */
  boolean isLocal();

  /** Returns the identifier that uniquely identifies this type of Storage. */
  String getStorageId();
}
