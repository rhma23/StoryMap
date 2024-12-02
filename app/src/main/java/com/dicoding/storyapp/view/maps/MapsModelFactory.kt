package com.dicoding.storyapp.view.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MapsViewModelFactory(private val repository: MapsRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsModel::class.java)) {
            return MapsModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}