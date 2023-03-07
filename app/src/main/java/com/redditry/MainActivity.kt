package com.redditry

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
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
    private var after: String? = ""
    private var sortBy: Post.SortBy = Post.SortBy.rising

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
            fun(adapter, _) {
                Thread {
                    if (after == null) {
                        binding.postList.lazyLoader?.loading = false
                        return@Thread
                    }
                    val res = posts.getPosts(sortBy, after)
                    if (res.first.size == 0)
                        binding.postList.lazyLoader?.loading = false
                    else
                        adapter.addPosts(res.first)
                    after = res.second
                    // No more posts
                    if (after == null) {
                        Handler(mainLooper).post { binding.postList.setProgressBar(false) }
                    }
                }.start()
            }
        )

        binding.postList.binding.sortBySpinner.onItemSelectedListener = object :
            OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sortBy = Post.SortBy.values()[p2]
                after = ""
                binding.postList.invalidate()
                binding.postList.adapter.clear()
                Handler(mainLooper).post { binding.postList.setProgressBar(true) }
                binding.postList.lazyLoader?.previousTotal = 0
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

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
