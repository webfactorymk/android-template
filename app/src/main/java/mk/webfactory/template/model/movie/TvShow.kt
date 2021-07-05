package mk.webfactory.template.model.movie

import com.google.gson.annotations.SerializedName

data class TvShow(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val title: String? = null,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("vote_average") val voteAverage: Float,
    @field:SerializedName("poster_path") val image: String? = null,
    @field:SerializedName("backdrop_path") val backdropImage: String? = null
) : Show() {
    override val mediaType: MediaType
        get() = MediaType.TV
}