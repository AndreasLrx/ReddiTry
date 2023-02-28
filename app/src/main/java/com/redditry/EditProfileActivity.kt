package com.redditry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redditry.databinding.ActivityProfileEditBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
