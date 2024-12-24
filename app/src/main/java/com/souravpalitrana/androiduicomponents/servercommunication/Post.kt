package com.souravpalitrana.androiduicomponents.servercommunication

import retrofit2.http.GET

data class Post(
    val userId: Int,
    val id: Int ,
    val title: String,
    val body: String
)