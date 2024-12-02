package com.dicoding.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.config.ApiService
import com.dicoding.storyapp.response.AddNewStoryResponse
import com.dicoding.storyapp.data.StoryPagingSource
import com.dicoding.storyapp.response.ListStoryItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(apiService) }
        ).liveData
    }

    suspend fun uploadNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddNewStoryResponse {
        return apiService.uploadNewStory("Bearer $token", photo, description, lat, lon)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}