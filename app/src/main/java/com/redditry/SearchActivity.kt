package com.redditry

import android.os.Bundle
import com.redditry.databinding.ActivitySearchBinding

class SearchActivity : ActivityHead() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnButton.setOnClickListener { finish() }

        // Activity Head
        navigationId = R.id.search_icon
        navBar = binding.navBar
    }
}
