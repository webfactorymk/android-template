package mk.webfactory.template.data.storage

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

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

    @CheckResult
    override fun save(item: T): Single<T> {
        return inner.save(item).doOnSuccess {
            cachedItem = it
            isItemFetched = true
        }
    }

    @CheckResult
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

    @CheckResult
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