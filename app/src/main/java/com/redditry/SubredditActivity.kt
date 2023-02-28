package com.redditry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redditry.databinding.ActivitySubredditBinding

class SubredditActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubredditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubredditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
