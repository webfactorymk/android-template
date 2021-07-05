package mk.webfactory.androidtemplate.network.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.MediaType
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import mk.webfactory.template.network.gson.RuntimeTypeAdapterFactory
import mk.webfactory.template.network.gson.fromJson
import org.junit.Test
import org.junit.Assert.*

/** Tests for paginated deserialization of [Show], [Movie], [TvShow]
 *
 * @see [RuntimeTypeAdapterFactory<Show>]
 * @see [PaginatedResponse<T>]
 */
class ShowGsonDeserializationTest {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory.of(Show::class.java, "media_type")
                .apply {
                    registerSubtype(Movie::class.java, "movie")
                    registerSubtype(TvShow::class.java, "tv")
                })
        .create()

    @Test
    fun gsonShowMultiTypeDeserialization() {
        val paginatedShowList: PaginatedResponse<Show> = gson.fromJson(apiResponse)

        print(paginatedShowList)

        assertEquals(1, paginatedShowList.page)
        assertEquals(1000, paginatedShowList.totalPages)
        assertEquals(2, paginatedShowList.items.size)
        assertTrue(paginatedShowList.items[0] is TvShow)
        assertEquals(MediaType.TV, paginatedShowList.items[0].mediaType)
        assertTrue(paginatedShowList.items[1] is Movie)
        assertEquals(MediaType.MOVIE, paginatedShowList.items[1].mediaType)
    }
}

val apiResponse = "" +
        "{\n" +
        "    \"page\": 1,\n" +
        "    \"results\": [\n" +
        "        {\n" +
        "            \"overview\": \"After stealing the Tesseract during the events of “Avengers: Endgame,” ....\",\n" +
        "            \"original_name\": \"Loki\",\n" +
        "            \"origin_country\": [\n" +
        "                \"US\"\n" +
        "            ],\n" +
        "            \"backdrop_path\": \"/wr7nrzDrpGCEgYnw15jyAB59PtZ.jpg\",\n" +
        "            \"genre_ids\": [\n" +
        "                18,\n" +
        "                10765\n" +
        "            ],\n" +
        "            \"first_air_date\": \"2021-06-09\",\n" +
        "            \"original_language\": \"en\",\n" +
        "            \"poster_path\": \"/kEl2t3OhXc3Zb9FBh1AuYzRTgZp.jpg\",\n" +
        "            \"vote_count\": 4824,\n" +
        "            \"id\": 84958,\n" +
        "            \"vote_average\": 8.1,\n" +
        "            \"name\": \"Loki\",\n" +
        "            \"popularity\": 4774.187,\n" +
        "            \"media_type\": \"tv\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"adult\": false,\n" +
        "            \"backdrop_path\": \"/8xt0ILZbqPsR8BVOIoY13ck6XtL.jpg\",\n" +
        "            \"genre_ids\": [\n" +
        "                27,\n" +
        "                35,\n" +
        "                9648\n" +
        "            ],\n" +
        "            \"vote_count\": 19,\n" +
        "            \"original_language\": \"en\",\n" +
        "            \"original_title\": \"Werewolves Within\",\n" +
        "            \"poster_path\": \"/WoG2tvwanOMlU0qsbxpf9Qd3y.jpg\",\n" +
        "            \"id\": 800497,\n" +
        "            \"video\": false,\n" +
        "            \"vote_average\": 7.5,\n" +
        "            \"title\": \"Werewolves Within\",\n" +
        "            \"overview\": \"When a proposed pipeline creates hostilities between residents of a small town, ....\",\n" +
        "            \"release_date\": \"2021-06-25\",\n" +
        "            \"popularity\": 35.468,\n" +
        "            \"media_type\": \"movie\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"total_pages\": 1000,\n" +
        "    \"total_results\": 20000\n" +
        "}"
