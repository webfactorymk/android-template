package mk.webfactory.template.data.storage

import io.reactivex.Completable
import io.reactivex.Observable
import mk.webfactory.template.data.rx.Observables.safeCompleteAfterPublish
import mk.webfactory.template.data.rx.Observables.safeCompleted
import mk.webfactory.template.data.rx.Observables.safeEndWithError

class InMemoryStorage <T> : Storage<T> {

    private var content: T? = null
    private var contentDeleted = false
    private var deleteCompletable: Completable? = null

    override fun save(t: T): Observable<T> {
        return Observable.just(t).doOnNext { t ->
            content = t
            contentDeleted = false
        }
    }

    override fun retrieve(): Observable<T> {
        return Observable.create { subscriber ->
            if (content == null && !contentDeleted) {
                safeEndWithError(subscriber, StorageException())
            } else {
                safeCompleteAfterPublish(subscriber, content)
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

    override val isLocal: Boolean
        get() = true

    override var storageId: String? = null
        get() {
            if (field == null) {
                field = "$TAG:" + (if (content != null) content::class.java else "")
            }
            return field
        }
        private set

    companion object {
        private val TAG = InMemoryStorage::class.java.simpleName
    }
}
