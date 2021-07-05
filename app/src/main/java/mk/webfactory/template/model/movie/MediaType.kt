package mk.webfactory.template.model.movie

import com.google.gson.annotations.SerializedName

enum class MediaType {
    @SerializedName("all") ALL,
    @SerializedName("movie") MOVIE,
    @SerializedName("tv") TV,
    @SerializedName("person") PERSON
}