package com.redditry

import android.os.Bundle
import com.redditry.components.SearchComponent
import com.redditry.databinding.ActivitySearchBinding

class SearchActivity : ActivityHead() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnButton.setOnClickListener { finish() }

        // display a list of subreddits when the user presses the search button
//        binding.searchButton.setOnClickListener {
//            val subredditName = binding.searchInput.text.toString()
//            if (subredditName.isNotEmpty()) {
//                val searchComponent = SearchComponent(subredditName)
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.search_results, SearchComponent)
//                    .commit()
//            }
//        }
        // search on the subreddits when the user presses enter
//        binding.searchInput.setOnEditorActionListener { _, _, _ ->
//            binding.searchButton.performClick()
//            true
//        }


        // Activity Head
        navigationId = R.id.search_icon
        navBar = binding.navBar
    }
}
