package mk.webfactory.template.data.storage;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import mk.webfactory.template.data.rx.StubObserver;

import static mk.webfactory.template.data.rx.Observables.safeCompleteAfterPublish;
import static mk.webfactory.template.data.rx.Observables.safeCompleted;
import static mk.webfactory.template.data.rx.Observables.safeEndWithError;

public final class InMemoryStorage<T> implements Storage<T> {

  private static final String TAG = InMemoryStorage.class.getSimpleName();

  private T content;
  private boolean contentDeleted;
  private Completable deleteCompletable;

  public InMemoryStorage() {
  }

  @Override public Observable<T> save(T t) {
    return Observable.just(t).doOnEach(new StubObserver<T>() {
      @Override public void onNext(T t) {
        content = t;
        contentDeleted = false;
      }
    });
  }

  @Override public Observable<T> retrieve() {
    return Observable.create(new ObservableOnSubscribe<T>() {
      @Override public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
        if (content == null && !contentDeleted) {
          safeEndWithError(subscriber, new StorageException());
        } else {
          safeCompleteAfterPublish(subscriber, content);
        }
      }
    });
  }

  @Override public Completable delete() {
    if (deleteCompletable == null) {
      deleteCompletable = Completable.create(new CompletableOnSubscribe() {
        @Override public void subscribe(@NonNull CompletableEmitter subscriber) throws Exception {
          T deletedContent = content;
          content = null;
          contentDeleted = true;
          safeCompleted(subscriber);
        }
      });
    }
    return deleteCompletable;
  }

  @Override public boolean isLocal() {
    return true;
  }

  private String storageId;

  @Override public String getStorageId() {
    if (storageId == null) {
      storageId = TAG + ":" + (content != null ? content.getClass() : "");
    }
    return storageId;
  }
}
