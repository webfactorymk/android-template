package mk.webfactory.storage

import io.reactivex.rxjava3.Completable
import io.reactivex.rxjava3.Maybe
import io.reactivex.rxjava3.Single

class InMemoryStorage<T> : Storage<T> {

    private var content: T? = null
        @Synchronized get
        @Synchronized set

    override val isLocal: Boolean = true
    override var storageId: String = InMemoryStorage::class.java.simpleName

    override fun save(item: T) = Single.fromCallable<T> {
        content = item
        content
    }

    override fun get() = Maybe.fromCallable<T> { content }

    override fun delete() = Completable.fromAction { content = null }
}
