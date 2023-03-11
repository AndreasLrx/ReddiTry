package com.redditry.components

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.redditry.LoginActivity
import com.redditry.R
import com.redditry.databinding.ComponentHeaderprofileBinding

class ProfileHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var binding: ComponentHeaderprofileBinding

    // inflate the component header profile layout
    init {
        LayoutInflater.from(context).inflate(R.layout.component_headerprofile, this, true)
        binding = ComponentHeaderprofileBinding.bind(this)

        attrs?.let {

            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.ProfileHeader, 0, 0)
            // remove token redirect to login
            binding.logoutButton.setOnClickListener {
                val sharedPref = context.getSharedPreferences("redditry", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    // remove accessToken and refreshToken from API
                    remove("redditToken")
                    remove("redditRefreshToken")
                    apply()
                }
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
            // change the text in the button if we are in the edit view
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
