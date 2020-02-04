package mk.webfactory.template.data.storage

import io.reactivex.Observable
import io.reactivex.functions.Function

object StorageHelpers {
    fun <T> deferSaveIn(storage: Storage<T>): Function<T, Observable<T>> {
        return deferSaveIn(storage, false)
    }

    /**
     * Unlike [deferSaveIn], the create observable will not be allowed to throw,
     * and instead the object being saved will be returned when an error occurs.
     */
    fun <T> deferSaveInAndIgnoreErrors(storage: Storage<T>): Function<T, Observable<T>> {
        return deferSaveIn(storage, true)
    }

    private fun <T> deferSaveIn(
        storage: Storage<T>,
        continueOnError: Boolean
    ): Function<T, Observable<T>> {
        return Function { t ->
            if (continueOnError) {
                storage.save(t)!!.onErrorReturn { t }
            } else storage.save(t)
        }
    }
}