package com.redditry

import android.content.Intent
import android.os.Bundle
import com.redditry.databinding.ActivityProfileBinding

class ProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileBinding
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
    }
}
