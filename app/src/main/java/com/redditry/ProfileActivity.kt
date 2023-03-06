package com.redditry

import android.content.Intent
import android.os.Bundle
import com.redditry.controller.User
import com.redditry.databinding.ActivityProfileBinding
import com.redditry.redditAPI.PostData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileBinding
    private val user = User()
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

        GlobalScope.launch {
            val profil = user.getMyProfil()
            val posts = user.getMyPost()
            runOnUiThread {
                binding.description.setUsername(if (profil?.name != null) profil.name else "Username")
                binding.description.setDescription(profil?.subreddit?.description.toString())
                binding.description.setFollowersNumber(profil?.subreddit?.subscribers.toString())
                profil?.icon_img?.let { binding.bannerAndPp.setImage(it) }
                profil?.subreddit?.banner_img?.let { binding.bannerAndPp.setBanner(it) }
                if (profil != null) {
                    if (!profil.subreddit.isAcceptingFollowers) {
                        binding.description.desactivateButton()
                    }
                }
                val postsData = ArrayList<PostData>()
                posts?.data?.children?.forEach {
                    postsData.add(it.data)
                }
                binding.posts.setPost(postsData.toTypedArray())
            }
        }
    }
}
