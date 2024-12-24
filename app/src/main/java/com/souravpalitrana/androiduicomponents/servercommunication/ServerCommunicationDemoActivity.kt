package com.souravpalitrana.androiduicomponents.servercommunication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.souravpalitrana.androiduicomponents.R
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class ServerCommunicationDemoActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
//    private var postViewModel: PostViewModel by viewModels() // won't work. why?
    private val postViewModel: PostViewModel by viewModels {
        PostViewModel.factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_communication_demo)
        tvResult = findViewById(R.id.tvtitle)
        val observer = Observer<List<Post>> { posts->
            if (posts != null) {
                tvResult.text = posts.first().title
            }else{
                tvResult.text = "No data found"
            }
        }
        postViewModel.posts.observe(this, observer)
        postViewModel.getAllPosts()
    }
}