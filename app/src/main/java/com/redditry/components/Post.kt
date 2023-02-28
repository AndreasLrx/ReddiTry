package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.redditry.R
import com.redditry.databinding.ComponentPostBinding

class Post @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: ComponentPostBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.component_post, this, true)
        binding = ComponentPostBinding.bind(this)

        attrs?.let {

            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.Post, 0, 0)

            // Post format
            val postFormat = styledAttributes.getInt(R.styleable.Post_post_format, 0)
            // Small
            if (postFormat == 1) {
                if (styledAttributes.hasValue(R.styleable.Post_image))
                    binding.content.maxLines = 3
                else
                    binding.content.maxLines = 8
            }

            // Background Color
            val backgroundColorId = styledAttributes.getInt(R.styleable.Post_post_color, 0)
            when (backgroundColorId) {
                0 -> setBackgroundColor(ContextCompat.getColor(context, R.color.purple_light))
                1 -> setBackgroundColor(ContextCompat.getColor(context, R.color.beige_light))
                2 -> setBackgroundColor(ContextCompat.getColor(context, R.color.semi_transparent))
            }

            // Subreddit icon
            val icon = styledAttributes.getDrawable(R.styleable.Post_subreddit_icon)
            if (icon != null)
                binding.subredditIcon.setImageDrawable(icon)
            // Subreddit name
            binding.subredditName.text =
                styledAttributes.getString(R.styleable.Post_subreddit)
            // Sender name
            binding.userName.text =
                styledAttributes.getString(R.styleable.Post_sender)
            // Post title
            binding.title.text =
                styledAttributes.getString(R.styleable.Post_post_title)

            // Post image (optional)
            val image = styledAttributes.getDrawable(R.styleable.Post_image)
            if (image != null) {
                binding.image.visibility = View.VISIBLE
                binding.image.setImageDrawable(image)
            } else
                binding.image.visibility = View.GONE

            // Post content (optional)
            val content = styledAttributes.getString(R.styleable.Post_post_content)
            if (content != null) {
                binding.content.visibility = View.VISIBLE
                binding.content.text = content
            } else
                binding.content.visibility = View.GONE

            // Post votes count
            binding.votes.text =
                styledAttributes.getInt(R.styleable.Post_votes_count, 0).toString()
            // Post comment count
            binding.comments.text =
                styledAttributes.getInt(R.styleable.Post_comments_count, 0).toString()

            binding.upvoteButton.setOnClickListener(
                createButtonClickListener {
                    println(
                        "Up Vote clicked"
                    )
                }
            )
            binding.downVoteButton.setOnClickListener(
                createButtonClickListener {
                    println(
                        "Down Vote clicked"
                    )
                }
            )
            binding.commentButton.setOnClickListener(
                createButtonClickListener {
                    println(
                        "Comment clicked"
                    )
                }
            )

            styledAttributes.recycle()
        }
    }

    private fun createButtonClickListener(onEndCallback: () -> Unit): View.OnClickListener {
        return View.OnClickListener {
            val anim = AnimationUtils.loadAnimation(this.context, R.anim.button_click)

            anim.interpolator = AccelerateInterpolator(0.7f)
            it.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    onEndCallback()
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }
            })
        }
    }
}
