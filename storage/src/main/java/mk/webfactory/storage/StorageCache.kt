package mk.webfactory.storage

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue

/**
 * Storage implementation with cache.
 * This component is threadsafe.
 */
class StorageCache<T>(private val inner: Storage<T>) : Storage<T> by inner {

    private var cachedItem: T? = null
        @Synchronized get
        @Synchronized set
    @Volatile
    private var isItemFetched = false

    @CheckReturnValue
    override fun save(item: T): Single<T> {
        return inner.save(item).doOnSuccess {
            cachedItem = it
            isItemFetched = true
        }
    }

    @CheckReturnValue
    override fun get(): Maybe<T> {
        return if (isItemFetched) {
            Maybe.fromCallable { cachedItem }
        } else {
            inner.get()
                .doOnSuccess {
                    cachedItem = it
                    isItemFetched = true
                }
                .doOnComplete {
                    cachedItem = null
                    isItemFetched = true
                }
        }
    }

    @CheckReturnValue
    override fun delete(): Completable {
        return inner.delete()
            .doOnComplete {
                cachedItem = null
                isItemFetched = true
            }
    }

    fun clearCache() {
        cachedItem = null
        isItemFetched = false
    }
}