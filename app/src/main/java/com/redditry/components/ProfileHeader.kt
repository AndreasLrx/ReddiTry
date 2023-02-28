package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.redditry.R
import com.redditry.databinding.ComponentHeaderprofileBinding

class ProfileHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding: ComponentHeaderprofileBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.component_headerprofile, this, true)
        binding = ComponentHeaderprofileBinding.bind(this)
    }
}
