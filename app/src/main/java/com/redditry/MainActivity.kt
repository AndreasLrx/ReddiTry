package com.redditry

import android.os.Bundle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.redditry.controller.Post
import com.redditry.controller.User
import com.redditry.databinding.ActivityMainBinding
import com.redditry.redditAPI.Subreddit

class MainActivity : ActivityHead() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout
    private val user = User()
    private val posts = Post()

    // On create inflate the layout and set the drawer component
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawer = binding.root
        binding.drawerComponent.drawer = drawer

        // ActivityHead setup
        navigationId = R.id.redditry_icon
        navBar = binding.navBar
        binding.postList.setLazyLoading(
            fun(
                sortBy: Post.SortBy,
                after: String
            ): Pair<ArrayList<com.redditry.redditAPI.Post>, String?> {
                return posts.getPosts(sortBy, after)
            }
        )

        Thread {
            val subreddits = user.getMySubscribedSubreddits()
            val subredditsData = ArrayList<Subreddit>()
            subreddits?.data?.children?.forEach {
                subredditsData.add(it.data)
            }
            binding.drawerComponent.adapter.data = subredditsData
        }.start()
    }

    override fun onNavBarItem(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_icon) {
            drawer.open()
        }
        return super.onNavBarItem(item)
    }
}
