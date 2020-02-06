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

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Generic single object store.
 *
 * Note that the storage may cache values for any subsequent queries.
 */
interface Storage<T> {

    /**
     * Saves the given item.
     *
     * @return The saved item
     * @throws StorageException If the item could not be saved
     * @see .isLocal
     */
    @CheckResult
    fun save(item: T): Single<T>

    /**
     * Gets the saved item, if any.
     *
     * @return Maybe that emits the saved item, or completes if no item exits
     * @throws StorageException
     */
    @CheckResult
    fun get(): Maybe<T>

    /**
     * Deletes any saved item. It's safe to call this even if no item is saved.
     *
     * @throws StorageException In case the item could not be deleted
     */
    @CheckResult
    fun delete(): Completable

    /**
     * Test whether the storage is local or remote.
     */
    val isLocal: Boolean

    /**
     * Returns the identifier that uniquely identifies this type of Storage.
     */
    val storageId: String
}