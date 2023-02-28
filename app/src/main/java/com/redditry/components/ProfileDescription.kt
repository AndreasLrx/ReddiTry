package com.redditry.components

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
        LayoutInflater.from(context).inflate(R.layout.component_description_profile, this, true)
        binding = ComponentDescriptionProfileBinding.bind(this)
    }

    fun setUsername(username:String){
        binding.profileUsername.text = username
    }
    fun setDescription(description:String){
        binding.profileDescription.text = description
    }
    fun setEmail(email:String){
        binding.profileEmail.text = email
    }
    fun setCountry(country:String){
        binding.profileCountry.text = country
    }
    fun setFollowersNumber(followersNumber:String){
        binding.profileFollowersNumber.text = followersNumber
    }
    fun desactivateButton(){
        binding.followButton.isEnabled = false
    }

}
