package com.redditry

import android.os.Bundle
import com.redditry.controller.Subreddit
import com.redditry.databinding.ActivitySubredditBinding

class SubredditActivity : ActivityHead() {
    private lateinit var binding: ActivitySubredditBinding
    private val subreddit: Subreddit = Subreddit()

    private lateinit var _title: String
    private var _members: Int = 0

    private var title: String
        get() = _title
        set(value) {
            _title = value
            binding.description.binding.subredditName.text = title
        }
    private var members: Int
        get() = _members
        set(value) {
            _members = value
            binding.description.binding.members.text =
                String.format(resources.getString(R.string.members), members)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubredditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationId = R.id.menu_icon
        navBar = binding.navBar

        val extras = intent.extras
        if (extras != null) {
            val name = extras.getString("subreddit_name", "r/Android")
            title = name
            Thread {
                val res = subreddit.getSubreddit(name) ?: return@Thread

                runOnUiThread {
                    members = res.subscribers
                    binding.description.binding.description.text = res.description
                    res.icon_img?.let { binding.bannerAndPp.setImage(it) }
                    res.mobile_banner_img?.let { binding.bannerAndPp.setBanner(it) }
                }
            }.start()
        }

    }
}
