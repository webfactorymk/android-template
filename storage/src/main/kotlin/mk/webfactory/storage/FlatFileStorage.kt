package mk.webfactory.storage

import io.reactivex.rxjava3.Completable
import io.reactivex.rxjava3.Maybe
import io.reactivex.rxjava3.Single
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

    override val isLocal: Boolean = true
    override var storageId: String = FlatFileStorage::class.java.simpleName

    override fun save(item: T): Single<T> {
        return Single.fromCallable {
            val contentString = parser.toJson(item!!)
            writeUtf8(contentString, contentFile)

            synchronized(storeInFieldLock) {
                content = item
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
            if (contentString != null && contentString.isNotEmpty()) {
                try {
                    content = parser.fromJson(contentString, type)
                } catch (e: Exception) {
                    deleteFile(contentFile)
                }
            }
            if (content != null) {
                synchronized(storeInFieldLock) {
                    this@FlatFileStorage.content = content
                }
            }

            content
        }
    }

    override fun delete(): Completable {
        return Completable.fromRunnable {
            synchronized(storeInFieldLock) {
                content = null
                contentDeleted = true
                if (!deleteFile(contentFile)) {
                    throw StorageException("Maybe file is a directory $contentFile"); }
            }
        }
    }
}
