package com.souravpalitrana.androiduicomponents.servercommunication

import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class PostRepository {
    private val apiService: ApiService = RetrofitClient.apiService
    suspend fun getAllPosts(): List<Post> {
        return withContext(Dispatchers.IO){
            apiService.getPosts()
        }

    }
}