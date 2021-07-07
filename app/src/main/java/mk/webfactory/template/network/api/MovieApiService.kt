package mk.webfactory.template.network.api

import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int = 1): Single<PaginatedResponse<Movie>>

    @GET("tv/popular")
    fun getPopularTvShows(@Query("page") page: Int = 1): Single<PaginatedResponse<TvShow>>

    @GET("trending/{mediaType}/{timeWindow}")
    fun getTrending(
        @Path("mediaType") mediaType: MediaType,
        @Path("timeWindow") timeWindow: TimeWindow,
        @Query("page") page: Int = 1,
        ): Single<PaginatedResponse<Show>>
}

fun getImageUrl(imgPath: String): String = "https://image.tmdb.org/t/p/w500/$imgPath"