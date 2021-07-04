package mk.webfactory.storage

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.annotations.CheckReturnValue

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
    @CheckReturnValue
    fun save(item: T): Single<T>

    /**
     * Gets the saved item, if any.
     *
     * @return Maybe that emits the saved item, or completes if no item exits
     * @throws StorageException
     */
    @CheckReturnValue
    fun get(): Maybe<T>

    /**
     * Deletes any saved item. It's safe to call this even if no item is saved.
     *
     * @throws StorageException In case the item could not be deleted
     */
    @CheckReturnValue
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