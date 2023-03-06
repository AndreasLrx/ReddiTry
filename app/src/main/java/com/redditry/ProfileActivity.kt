package com.redditry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.redditry.controller.UserController
import com.redditry.databinding.ActivityProfileBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileBinding
    private val userController = UserController()
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
            val profil = userController.getMyProfil()
            runOnUiThread {
                binding.description.setUsername(if (profil?.name != null) profil.name else "Username")
                binding.description.setDescription(profil?.subreddit?.description.toString())
                binding.description.setFollowersNumber(profil?.subreddit?.subscribers.toString())
                profil?.icon_img?.let { binding.bannerAndPp.setImage(it) }
                profil?.subreddit?.banner_img?.let { binding.bannerAndPp.setBanner(it) }
                if (profil != null) {
                    if (!profil.subreddit.isAcceptingFollowers){
                        binding.description.desactivateButton()
                    }
                }
            }
            val posts = userController.getMyPost()
            binding.posts
        }

    }
}
