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
    var binding: ComponentHeaderprofileBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.component_headerprofile, this, true)
        binding = ComponentHeaderprofileBinding.bind(this)

        attrs?.let {

            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.ProfileHeader, 0, 0)

            if (styledAttributes.getBoolean(R.styleable.ProfileHeader_read_only, false))
                visibility = GONE
            else {
                if (styledAttributes.getBoolean(R.styleable.ProfileHeader_editing, false)) {
                    binding.editButton.text = context.getString(R.string.exit_button)
                    binding.logoutButton.visibility = GONE
                } else
                    binding.editButton.text = context.getString(R.string.edit_button)
            }

            styledAttributes.recycle()
        }
    }
}
