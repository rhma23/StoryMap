package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.response.ListStoryItem
import com.dicoding.storyapp.data.repository.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope) // Cache untuk menyimpan hasil paging
}