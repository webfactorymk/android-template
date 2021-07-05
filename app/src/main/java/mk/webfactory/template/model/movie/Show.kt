package mk.webfactory.template.model.movie


/** Base class for Shows: Movie, TV Show, etc. */
sealed class Show {
    abstract val mediaType: MediaType
}
