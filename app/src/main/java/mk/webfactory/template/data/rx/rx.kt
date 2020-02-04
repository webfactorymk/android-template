package mk.webfactory.template.data.rx

import io.reactivex.disposables.Disposable

fun Disposable?.safeDispose(): Disposable? {
    if (this != null && !this.isDisposed) {
        this.dispose()
    }
    return null
}