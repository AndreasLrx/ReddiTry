package com.redditry

import android.os.Bundle
import com.redditry.databinding.ActivityProfileEditBinding

class EditProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.binding.editButton.setOnClickListener { finish() }

        navigationId = R.id.profil_icon
        navBar = binding.navBar
    }
}
