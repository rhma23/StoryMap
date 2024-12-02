package com.dicoding.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.config.ApiService
import com.dicoding.storyapp.response.ListStoryItem

class MapsRepository(private val apiService: ApiService) {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun getStoriesWithLocation(token: String) {
        try {
            val response = apiService.getStoriesWithLocation("Bearer $token")
            Log.d("StoriesRepository", "getAllData on Response: $response")
            _stories.postValue(response.listStory as List<ListStoryItem>?)
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.message}")
            Log.e("StoriesRepository", "getAllData on Exception: ${e.message}")
        }
    }
}