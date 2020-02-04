package mk.webfactory.template.data.storage

class StorageException : RuntimeException {
    constructor()
    constructor(detailMessage: String?) : super(detailMessage)
    constructor(detailMessage: String?, throwable: Throwable?) : super(detailMessage, throwable)
    constructor(throwable: Throwable?) : super(throwable)
}
