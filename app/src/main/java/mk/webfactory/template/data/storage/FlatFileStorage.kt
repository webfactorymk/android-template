package mk.webfactory.template.data.storage

import android.text.TextUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import mk.webfactory.template.data.rx.Observables.safeCompleteAfterPublish
import mk.webfactory.template.data.rx.Observables.safeCompleted
import mk.webfactory.template.data.rx.Observables.safeEndWithError
import java.io.File
import java.lang.reflect.Type

/** [Storage] that saves and retrieves an object from a file.  */
class FlatFileStorage<T>(
        private val type: Type,
        private val contentFile: File,
        private val parser: JsonConverter) : Storage<T> {

    private val storeInFieldLock = Any()
    private var content: T? = null
    private var contentDeleted = false
    private lateinit var retrieveDataObservable: Observable<T>

    override val isLocal: Boolean = true
    override var storageId: String = FlatFileStorage::class.java.simpleName

    override fun save(t: T): Observable<T> {
        return Observable.create(ObservableOnSubscribe { subscriber ->
            if (t == null) {
                safeEndWithError(subscriber, StorageException())
                return@ObservableOnSubscribe
            }
            val successful: Boolean
            successful = try {
                val contentString = parser.toJson(t)
                Util.writeUtf8(contentString, contentFile)
            } catch (e: Exception) {
                false
            }
            if (successful) {
                synchronized(storeInFieldLock) {
                    content = t
                    contentDeleted = false
                    safeCompleteAfterPublish(subscriber, content)
                }
            }
        })
    }

    override fun retrieve(): Observable<T> {
        retrieveDataObservable = Observable.create(ObservableOnSubscribe { subscriber ->
            synchronized(storeInFieldLock) {
                if (content != null || contentDeleted) {
                    subscriber.onNext(content!!)
                    subscriber.onComplete()
                    return@ObservableOnSubscribe
                }
            }
            var content: T? = null
            val contentString = Util.readFullyUtf8(contentFile)
            var exception: Exception? = null
            if (!TextUtils.isEmpty(contentString)) {
                try {
                    content = parser.fromJson(contentString!!, type)
                } catch (e: Exception) {
                    Util.deleteFile(contentFile)
                    exception = e
                }
            }
            if (content != null) {
                synchronized(storeInFieldLock) { this@FlatFileStorage.content = content }
            }
            content?.let { safeCompleteAfterPublish(subscriber, it) }
                    ?: safeEndWithError(subscriber,
                            StorageException("Failed to read file $contentFile", exception))
        })
        return retrieveDataObservable
    }

    override fun delete(): Completable {
        return Completable.create { subscriber ->
            synchronized(storeInFieldLock) {
                // the file content should be read
                // but nobody cares about the deleted content
                val deletedContent = content
                content = null
                contentDeleted = true
                if (Util.deleteFile(contentFile)) {
                    safeCompleted(subscriber)
                } else {
                    safeEndWithError(subscriber, StorageException("Maybe file is a directory $contentFile"))
                }
            }
        }
    }
}
