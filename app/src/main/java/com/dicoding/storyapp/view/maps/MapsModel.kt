package com.dicoding.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.response.ListStoryItem
import com.dicoding.storyapp.util.SessionManager
import kotlinx.coroutines.launch

class MapsModel(private val repository: MapsRepository) : ViewModel() {
    private lateinit var sessionManager: SessionManager
    val stories: LiveData<List<ListStoryItem>> = repository.stories
    val errorMessage: LiveData<String> = repository.errorMessage

    fun setSessionManager(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
    }

    fun fetchAllStoriesWithLocation() {
        viewModelScope.launch {
            val token = sessionManager.getToken() ?: ""
            val response = repository.getStoriesWithLocation(token)
            Log.d("MapsModel", "fetchAllStoriesWithLocation: $response")
        }
    }
}
