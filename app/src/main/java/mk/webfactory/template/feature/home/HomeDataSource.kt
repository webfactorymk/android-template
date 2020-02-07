package mk.webfactory.template.feature.home

import io.reactivex.Completable
import io.reactivex.Maybe

interface HomeDataSource {
    fun setSomething(any: Any): Unit
    fun getSomething(): Maybe<Any>
    fun deleteData(): Completable
    val isDataAvailable: Boolean
}