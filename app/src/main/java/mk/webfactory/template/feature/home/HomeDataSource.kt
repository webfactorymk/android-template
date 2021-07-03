package mk.webfactory.template.feature.home

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface HomeDataSource {
    fun setSomething(any: Any): Unit
    fun getSomething(): Maybe<Any>
    fun deleteData(): Completable
    val isDataAvailable: Boolean
}