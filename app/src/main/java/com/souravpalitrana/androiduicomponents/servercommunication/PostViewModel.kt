package com.souravpalitrana.androiduicomponents.servercommunication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    fun getAllPosts() {
        viewModelScope.launch {
            _posts.value = postRepository.getAllPosts()
            try {
                _posts.value = postRepository.getAllPosts()
            } catch (e: Exception) {
                _posts.value = emptyList()
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
                    return PostViewModel(PostRepository()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}