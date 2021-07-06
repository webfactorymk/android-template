package mk.webfactory.template.model.api

import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.functions.Function

data class PaginatedResponse<T>(
    @field:SerializedName("page") val page: Int,
    @field:SerializedName("total_pages") val totalPages: Int,
    @field:SerializedName("results") val items: List<T>
)

fun <T> empty() = PaginatedResponse<T>(0, 0, emptyList())

fun <T> paginatedItemsMapper(): Function<PaginatedResponse<T>, List<T>> {
    return Function<PaginatedResponse<T>, List<T>> { response -> response.items }
}