/*
 * MIT License
 *
 * Copyright (c) 2020 Web Factory LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mk.webfactory.template.data.storage

import android.text.TextUtils
import io.reactivex.*
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

    fun safeCompleted(subscriber: CompletableEmitter) {
        if (!subscriber.isDisposed) {
            subscriber.onComplete()
        }
    }

    fun safeEndWithError(subscriber: CompletableEmitter, e: Throwable?) {
        if (!subscriber.isDisposed) {
            subscriber.onError(e!!)
        }
    }
}
