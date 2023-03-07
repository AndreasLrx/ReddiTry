package com.redditry

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.redditry.controller.User
import com.redditry.databinding.ActivityProfileBinding

class ProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileBinding
    private val user = User()

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

        Thread {
            val profile = user.getMyProfil()

            runOnUiThread {
                binding.description.setUsername(if (profile?.name != null) profile.name else "Username")
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

            // / Posts have a progress bar and can therefore be loaded later to speed up basic informations
            val posts = user.getMyPost()
            runOnUiThread {
                val postsData = ArrayList<com.redditry.redditAPI.Post>()
                posts?.data?.children?.forEach {
                    postsData.add(it.data)
                }
                binding.posts.setPost(postsData)
            }
        }.start()
    }
}
