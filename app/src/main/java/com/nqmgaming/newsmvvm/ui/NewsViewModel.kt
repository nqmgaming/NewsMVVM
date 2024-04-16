package com.nqmgaming.newsmvvm.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.newsmvvm.model.Article
import com.nqmgaming.newsmvvm.model.NewsResponse
import com.nqmgaming.newsmvvm.repository.NewsRepository
import com.nqmgaming.newsmvvm.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newRepository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    var breakingNewsResponse: NewsResponse? = null

    init {
        try {
            getBreakingNews("us")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch(Dispatchers.IO) {
        breakingNews.postValue(Resource.Loading())
        val response = newRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        searchNews.postValue(Resource.Loading())
        val response = newRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Resource.Success(resultResponse)
            } ?: Resource.Error("No data found")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Resource.Success(resultResponse)
            } ?: Resource.Error("No data found")
        } else {
            Resource.Error(response.message())
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newRepository.upsert(article)
    }

    fun getSavedNews() = newRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newRepository.deleteArticle(article)
    }
}