package mk.webfactory.template.data.storage

import android.text.TextUtils
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import mk.webfactory.template.data.rx.Observables.safeCompleted
import mk.webfactory.template.data.rx.Observables.safeEndWithError
import java.io.File
import java.lang.reflect.Type

/** [Storage] that saves and retrieves an object from a file.  */
class FlatFileStorage<T>(
    private val type: Type,
    private val contentFile: File,
    private val parser: JsonConverter
) : Storage<T> {

    private val storeInFieldLock = Any()
    private var content: T? = null
    private var contentDeleted = false
    private lateinit var retrieveDataObservable: Observable<T>

    override val isLocal: Boolean = true
    override var storageId: String = FlatFileStorage::class.java.simpleName

    override fun save(t: T): Single<T> {
        return Single.fromCallable {
            val contentString = parser.toJson(t!!)
            writeUtf8(contentString, contentFile)

            synchronized(storeInFieldLock) {
                content = t
                contentDeleted = false
            }
            content
        }
    }

    override fun get(): Maybe<T> {
        return Maybe.fromCallable<T> {
            synchronized(storeInFieldLock) {
                if (content != null || contentDeleted) {
                    content
                }
            }

            var content: T? = null
            val contentString = readFullyUtf8(contentFile)
            if (!TextUtils.isEmpty(contentString)) {
                try {
                    content = parser.fromJson(contentString!!, type)
                } catch (e: Exception) {
                    deleteFile(contentFile)
                }
            }
            if (content != null) {
                synchronized(storeInFieldLock) {
                    this@FlatFileStorage.content = content
                }
            }

            content?.let { it }
        }
    }

    override fun delete(): Completable {
        return Completable.create { subscriber ->
            synchronized(storeInFieldLock) {
                // the file content should be read
                // but nobody cares about the deleted content
                val deletedContent = content
                content = null
                contentDeleted = true
                if (deleteFile(contentFile)) {
                    safeCompleted(subscriber)
                } else {
                    safeEndWithError(
                        subscriber,
                        StorageException("Maybe file is a directory $contentFile")
                    )
                }
            }
        }
    }
}
