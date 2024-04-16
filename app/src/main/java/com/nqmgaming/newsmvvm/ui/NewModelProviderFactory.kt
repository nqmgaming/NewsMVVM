package com.nqmgaming.newsmvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nqmgaming.newsmvvm.repository.NewsRepository

class NewModelProviderFactory(
    val newRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newRepository) as T
    }
}