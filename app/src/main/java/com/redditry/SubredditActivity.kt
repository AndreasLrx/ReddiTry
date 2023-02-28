package com.redditry

import android.os.Bundle
import com.redditry.databinding.ActivitySubredditBinding

class SubredditActivity : ActivityHead() {
    private lateinit var binding: ActivitySubredditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubredditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null)
            binding.description.binding.subredditName.text = extras.getString("subreddit_name")

        navigationId = R.id.menu_icon
        navBar = binding.navBar
    }
}
