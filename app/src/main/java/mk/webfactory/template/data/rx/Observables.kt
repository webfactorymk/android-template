package mk.webfactory.template.data.rx

import io.reactivex.CompletableEmitter
import io.reactivex.ObservableEmitter

object Observables {
    /** Calls [ObservableEmitter.onNext] on the ObservableEmitter if it's still subscribed.  */
    fun <T> safePublishNext(subscriber: ObservableEmitter<T>, data: T) {
        if (!subscriber.isDisposed) {
            subscriber.onNext(data)
        }
    }

    /**
     * [ObservableEmitter.onNext] with [data] before it
     * [ObservableEmitter.onComplete]  completes}, only if the ObservableEmitter is still subscribed.
     */
    fun <T> safeCompleteAfterPublish(subscriber: ObservableEmitter<T>, data: T) {
        if (!subscriber.isDisposed) {
            subscriber.onNext(data)
            subscriber.onComplete()
        }
    }

    fun safeCompleted(subscriber: ObservableEmitter<*>) {
        if (!subscriber.isDisposed) {
            subscriber.onComplete()
        }
    }

    fun safeCompleted(subscriber: CompletableEmitter) {
        if (!subscriber.isDisposed) {
            subscriber.onComplete()
        }
    }

    fun <T> safeCompleteAfterEachPublish(subscriber: ObservableEmitter<T>, data: Array<T>) {
        if (!subscriber.isDisposed) {
            for (i in data.indices) {
                subscriber.onNext(data[i])
            }
            subscriber.onComplete()
        }
    }

    fun safeEndWithError(subscriber: ObservableEmitter<*>, e: Throwable?) {
        if (!subscriber.isDisposed) {
            subscriber.onError(e!!)
        }
    }

    fun safeEndWithError(subscriber: CompletableEmitter, e: Throwable?) {
        if (!subscriber.isDisposed) {
            subscriber.onError(e!!)
        }
    }
}