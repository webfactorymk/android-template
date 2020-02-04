package mk.webfactory.template.data.storage;

import android.text.TextUtils;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import java.io.File;
import java.lang.reflect.Type;

import static mk.webfactory.template.data.rx.Observables.safeCompleteAfterPublish;
import static mk.webfactory.template.data.rx.Observables.safeCompleted;
import static mk.webfactory.template.data.rx.Observables.safeEndWithError;
import static mk.webfactory.template.data.storage.Util.deleteFile;
import static mk.webfactory.template.data.storage.Util.readFullyUtf8;

/** {@link Storage} that saves and retrieves an object from a file. */
public final class FlatFileStorage<T> implements Storage<T> {

  private static final String TAG = FlatFileStorage.class.getSimpleName();

  private final Object storeInFieldLock = new Object();
  private final File contentFile;
  private final JsonConverter parser;
  private final Type type;
  private T content;
  private boolean contentDeleted;
  private Observable<T> retrieveDataObservable;

  public FlatFileStorage(Type type, File contentFile, JsonConverter parser) {
    this.contentFile = contentFile;
    this.parser = parser;
    this.type = type;
  }

  @Override public Observable<T> save(final T t) {
    return Observable.create(new ObservableOnSubscribe<T>() {
      @Override public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
        if (t == null) {
          safeEndWithError(subscriber, new StorageException());
          return;
        }
        boolean successful;
        try {
          String contentString = parser.toJson(t);
          successful = Util.writeUtf8(contentString, contentFile);
        } catch (Exception e) {
          successful = false;
        }
        if (successful) {
          synchronized (storeInFieldLock) {
            content = t;
            contentDeleted = false;
            safeCompleteAfterPublish(subscriber, content);
          }
        }
      }
    });
  }

  @Override public Observable<T> retrieve() {
    if (retrieveDataObservable != null) {
      return retrieveDataObservable;
    }
    retrieveDataObservable = Observable.create(new ObservableOnSubscribe<T>() {
      @Override public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
        synchronized (storeInFieldLock) {
          if (content != null || contentDeleted) {
            subscriber.onNext(content);
            subscriber.onComplete();
            return;
          }
        }

        T content = null;
        String contentString = readFullyUtf8(contentFile);
        Exception exception = null;
        if (!TextUtils.isEmpty(contentString)) {
          try {
            content = parser.fromJson(contentString, type);
          } catch (Exception e) {
            deleteFile(contentFile);
            exception = e;
          }
        }
        if (content != null) {
          synchronized (storeInFieldLock) {
            FlatFileStorage.this.content = content;
          }
        }
        if (content == null) {
          safeEndWithError(subscriber,
              new StorageException("Failed to read file " + contentFile, exception));
        } else {
          safeCompleteAfterPublish(subscriber, content);
        }
      }
    });
    return retrieveDataObservable;
  }

  @Override public Completable delete() {
    return Completable.create(new CompletableOnSubscribe() {
      @Override public void subscribe(@NonNull CompletableEmitter subscriber) throws Exception {
        synchronized (storeInFieldLock) {
          // the file content should be read
          // but nobody cares about the deleted content
          T deletedContent = content;
          content = null;
          contentDeleted = true;
          if (deleteFile(contentFile)) {
            safeCompleted(subscriber);
          } else {
            safeEndWithError(subscriber, new StorageException("Maybe file is a directory " + contentFile));
          }
        }
      }
    });
  }

  @Override public boolean isLocal() {
    return true;
  }

  private String storageId;

  @Override public String getStorageId() {
    if (storageId == null) {
      storageId = TAG + ":" + type.toString();
    }
    return storageId;
  }
}
