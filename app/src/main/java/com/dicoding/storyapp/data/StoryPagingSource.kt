package com.dicoding.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.config.ApiService
import com.dicoding.storyapp.response.ListStoryItem

class StoryPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories(position, params.loadSize)

            // Mengatasi kemungkinan null pada listStory dan elemen di dalamnya
            val stories = response.listStory?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = stories, // Daftar yang sudah bersih dari null
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
