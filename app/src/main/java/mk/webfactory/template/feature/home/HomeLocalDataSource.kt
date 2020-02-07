package mk.webfactory.template.feature.home

import io.reactivex.Completable
import io.reactivex.Maybe
import mk.webfactory.storage.InMemoryStorage

/**
 * Concrete implementation of a data source as in memory cache.
 */
class HomeLocalDataSource(private val userId: String, private val storage: InMemoryStorage<Any>) :
    HomeDataSource {
    private var isCacheCorrupt = true

    override fun getSomething(): Maybe<Any> {
        return if (!isCacheCorrupt) {
            storage.get()
        } else {
            Maybe.empty()
        }
    }

    override fun setSomething(any: Any) {
        //    storage.save(object).subscribeBy();
        isCacheCorrupt = false
    }

    override fun deleteData(): Completable {
        isCacheCorrupt = true
        return storage.delete()
    }

    override val isDataAvailable: Boolean
        get() = !isCacheCorrupt

}