package com.redditry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.redditry.components.AdapterSubredditList
import com.redditry.controller.Subreddit
import com.redditry.databinding.ActivitySearchBinding

class SearchActivity : ActivityHead() {
    private lateinit var binding: ActivitySearchBinding
    lateinit var adapter: AdapterSubredditList
    private var subreddit: Subreddit = Subreddit()

    // on create inflate the layout and set the return button to finish the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnButton.setOnClickListener { finish() }

        // Activity Head
        navigationId = R.id.search_icon
        navBar = binding.navBar

        // Search
        adapter = AdapterSubredditList(this)
        binding.searchResults.adapter = adapter
        binding.searchButton.setOnClickListener {
            val text = binding.searchInput.text
            if (text != null && text.isNotEmpty()) {
                binding.progressbar.visibility = View.VISIBLE
                Thread {
                    val subreddits = subreddit.search(text.toString())
                    val subredditsData = ArrayList<com.redditry.redditAPI.Subreddit>()
                    subreddits?.data?.children?.forEach {
                        subredditsData.add(it.data)
                    }
                    adapter.data = subredditsData
                    runOnUiThread {
                        binding.progressbar.visibility = View.GONE
                        showHint(subredditsData.size == 0)
                    }
                }.start()
            } else
                showHint()
        }

        // On click on a subreddit open the subreddit activity
        binding.searchResults.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, i, _ ->
                val intent = Intent(view.context, SubredditActivity::class.java)
                intent.putExtra("subreddit_name", adapter.data[i].displayName)
                view.context.startActivity(intent)
            }
    }

    // Show the hint if there is no result
    fun showHint(hintVisible: Boolean = true) {
        if (hintVisible) {
            binding.searchHint.visibility = View.VISIBLE
            binding.searchResults.visibility = View.GONE
        } else {
            binding.searchHint.visibility = View.GONE
            binding.searchResults.visibility = View.VISIBLE
        }
    }
}
