package com.redditry.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.redditry.R
import com.redditry.databinding.ComponentDescriptionProfileBinding

class ProfileDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ComponentDescriptionProfileBinding

    init {
        LayoutInflater.from(context)
            .inflate(com.redditry.R.layout.component_description_profile, this, true)
        binding = ComponentDescriptionProfileBinding.bind(this)
    }

    fun setUsername(username: String) {
        binding.profileUsername.text = username
    }

    fun getUsername(): String {
        return binding.profileUsername.text.toString()
    }

    fun setDescription(description: String) {
        binding.profileDescription.text = description
    }

    fun getDescription(): String {
        return binding.profileDescription.text.toString()
    }

    fun setUnderage(value: Boolean) {
        if (value) {
            binding.profileUnderage.text = "Age: +18"
        } else {
            binding.profileUnderage.text = "Age: -18"
        }
    }

    fun getUnderage(): Boolean {
        return binding.profileUnderage.text == "Age: +18"
    }

    fun setCountry(country: String) {
        binding.profileCountry.text = country
    }

    fun getCountry(): String {
        return binding.profileCountry.text.toString()
    }

    @SuppressLint("SetTextI18n")
    fun setFollowersNumber(followersNumber: String) {
        val followers = resources.getString(com.redditry.R.string.followers)
        binding.profileFollowersNumber.text = "$followersNumber $followers"
    }

    fun desactivateButton() {
        binding.followButton.isEnabled = false
    }
}
