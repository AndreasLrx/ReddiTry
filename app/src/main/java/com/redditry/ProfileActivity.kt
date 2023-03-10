package com.redditry

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.redditry.controller.Post
import com.redditry.controller.User
import com.redditry.databinding.ActivityProfileBinding

class ProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileBinding
    private val user = User()
    private var username: String? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBar.selectedItemId = R.id.profil_icon
        navBar = binding.navBar

        // ActivityHead setup
        navigationId = R.id.profil_icon
        navBar = binding.navBar

        val extras = intent.extras
        if (extras != null) {
            username = extras.getString("user_name")
            if (username != null)
                binding.header.visibility = View.GONE
        }

        binding.header.binding.editButton.setOnClickListener {
            openActivity(
                EditProfileActivity::class.java,
                Intent.FLAG_ACTIVITY_NO_HISTORY
            )
        }

        binding.posts.binding.postList.listView.setOnTouchListener(
            View.OnTouchListener { _, event ->
                binding.scrollView.requestDisallowInterceptTouchEvent(true)
                when (event.actionMasked) {
                    MotionEvent.ACTION_UP -> binding.scrollView.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
                false
            }
        )
        binding.posts.binding.postList.binding.sortBySpinner.visibility = View.GONE

        Thread {
            val profile = if (username == null)
                user.getMyProfil()
            else
                user.getUser(username!!)

            username = profile?.name

            runOnUiThread {
                binding.description.setUsername(if (username != null) username!! else "Username")
                binding.description.setDescription(profile?.subreddit?.description.toString())
                binding.description.setFollowersNumber(profile?.subreddit?.subscribers.toString())
                profile?.icon_img?.let { binding.bannerAndPp.setImage(it) }
                profile?.subreddit?.banner_img?.let { binding.bannerAndPp.setBanner(it) }
                if (profile != null) {
                    if (!profile.subreddit.isAcceptingFollowers) {
                        binding.description.desactivateButton()
                    }
                }
            }

            binding.posts.binding.postList.setLazyLoading(
                fun(
                    _: Post.SortBy,
                    after: String
                ): Pair<ArrayList<com.redditry.redditAPI.Post>, String?> {
                    return if (username == null)
                        Pair(ArrayList(), null)
                    else
                        user.getMyPost(username!!, after)
                }
            )
        }.start()
    }
}
