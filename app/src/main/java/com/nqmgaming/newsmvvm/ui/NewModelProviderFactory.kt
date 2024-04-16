package com.nqmgaming.newsmvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nqmgaming.newsmvvm.NewsApplication
import com.nqmgaming.newsmvvm.repository.NewsRepository

class NewModelProviderFactory(
    val app: NewsApplication,
    val newRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newRepository, app) as T
    }
}