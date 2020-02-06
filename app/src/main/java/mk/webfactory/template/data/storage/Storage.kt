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