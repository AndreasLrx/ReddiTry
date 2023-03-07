package com.redditry.components

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.redditry.R
import com.redditry.databinding.ComponentPhotoBinding

class ProfilePicture @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var binding: ComponentPhotoBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.component_photo, this, true)
        binding = ComponentPhotoBinding.bind(this)

        attrs?.let {
            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.ProfilePicture, 0, 0)
            // if profilePicture_read_only is true, the button is not visible
            if (styledAttributes.getBoolean(R.styleable.ProfilePicture_bla, false)) {
                binding.editButtonRound.visibility = VISIBLE
                binding.editButtonRoundBanner.visibility = VISIBLE
            }
            styledAttributes.recycle()
        }

        binding.editButtonBanner.setOnClickListener {
            println("editButtonRound clicked")
        }

        binding.editButtonPp.setOnClickListener {
            println("editButtonRoundBanner clicked")
        }
    }

    fun setImage(imageUrl: String) {
        Glide.with(this).load(Uri.parse(imageUrl)).into(binding.picture)
    }

    fun setBanner(bannerUrl: String) {
        Glide.with(this).load(Uri.parse(bannerUrl)).into(binding.banner)
    }


}
