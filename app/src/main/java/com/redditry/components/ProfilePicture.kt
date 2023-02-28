package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
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
    }
}
