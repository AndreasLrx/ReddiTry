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
}
