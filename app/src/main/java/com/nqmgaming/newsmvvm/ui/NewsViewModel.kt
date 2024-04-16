package com.nqmgaming.newsmvvm.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    val newRepository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        try {
            getBreakingNews("us")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch(Dispatchers.IO) {
        breakingNews.postValue(Resource.Loading())
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        searchNews.postValue(Resource.Loading())
        safeSearchNewsCall(searchQuery)
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                Resource.Success(breakingNewsResponse ?: resultResponse)
            } ?: Resource.Error("No data found")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                Resource.Success(searchNewsResponse ?: resultResponse)
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

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (newRepository.isNetworkAvailable(getApplication())) {
                val response = newRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is Exception -> breakingNews.postValue(Resource.Error("An error occurred: ${t.message}"))
                else -> breakingNews.postValue(Resource.Error("An error occurred"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (newRepository.isNetworkAvailable(getApplication())) {
                val response = newRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is Exception -> searchNews.postValue(Resource.Error("An error occurred: ${t.message}"))
                else -> searchNews.postValue(Resource.Error("An error occurred"))
            }
        }
    }
}