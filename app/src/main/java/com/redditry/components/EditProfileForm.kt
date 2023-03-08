package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.redditry.R
import com.redditry.databinding.ComponentFormEditProfileBinding

class EditProfileForm @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    var binding: ComponentFormEditProfileBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.component_form_edit_profile, this, true)
        binding = ComponentFormEditProfileBinding.bind(this)
    }
}
