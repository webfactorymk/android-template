package mk.webfactory.template.feature.home.ui.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.Show

const val DEFAULT_PAGE_INDEX = 1

class ShowPagingSource<T : Show>(val dataProvider: (page: Int) -> PaginatedResponse<T>) :
    PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: DEFAULT_PAGE_INDEX

        return try {
            val paginatedResponse = dataProvider(page)
            LoadResult.Page(
                data = paginatedResponse.items,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (paginatedResponse.page == paginatedResponse.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.anchorPosition
}