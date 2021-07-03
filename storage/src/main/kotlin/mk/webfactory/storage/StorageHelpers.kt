package mk.webfactory.storage

import io.reactivex.rxjava3.rxjava3.Single
import io.reactivex.rxjava3.rxjava3.functions.Function

fun <T> deferSaveIn(storage: Storage<T>): Function<T, Single<T>> {
    return deferSaveIn(storage, false)
}

/**
 * Unlike [deferSaveIn], the create observable will not be allowed to throw,
 * and instead the object being saved will be returned when an error occurs.
 */
fun <T> deferSaveInAndIgnoreErrors(storage: Storage<T>): Function<T, Single<T>> {
    return deferSaveIn(storage, true)
}

private fun <T> deferSaveIn(
    storage: Storage<T>,
    continueOnError: Boolean
): Function<T, Single<T>> {
    return Function { t ->
        if (continueOnError) {
            storage.save(t).onErrorReturn { t }
        } else storage.save(t)
    }
}