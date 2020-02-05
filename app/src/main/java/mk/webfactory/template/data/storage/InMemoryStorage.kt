package mk.webfactory.template.data.storage

import io.reactivex.Completable
import io.reactivex.Single
import mk.webfactory.template.data.rx.Observables.safeCompleted

class InMemoryStorage<T> : Storage<T> {

    private var content: T? = null
    private var contentDeleted = false
    private var deleteCompletable: Completable? = null

    override val isLocal: Boolean = true
    override var storageId: String = FlatFileStorage::class.java.simpleName

    override fun save(t: T): Single<T> {
        return Single.fromCallable {
            content = t
            contentDeleted = false
            content
        }
    }

    override fun retrieve(): Single<T> {
        return Single.fromCallable {
            if (content == null && !contentDeleted) {
                throw StorageException()
            } else {
                content
            }
        }
    }

    override fun delete(): Completable {
        if (deleteCompletable == null) {
            deleteCompletable = Completable.create { subscriber ->
                val deletedContent = content
                content = null
                contentDeleted = true
                safeCompleted(subscriber)
            }
        }
        return deleteCompletable!!
    }
}
