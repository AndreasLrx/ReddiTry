package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.redditry.R

class PostComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val currentFractionDigit = 2

    init {
        LayoutInflater.from(context).inflate(R.layout.component_post, this, true)
        attrs?.let {
            val postContentView = findViewById<TextView>(R.id.component_post_content)

            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.PostComponent, 0, 0)

            // Post format
            val postFormat = styledAttributes.getInt(R.styleable.PostComponent_post_format, 0)
            // Small
            if (postFormat == 1) {
                if (styledAttributes.hasValue(R.styleable.PostComponent_image))
                    postContentView.maxLines = 3
                else
                    postContentView.maxLines = 8
            }

            // Background Color
            val backgroundColorId = styledAttributes.getInt(R.styleable.PostComponent_post_color, 0)
            if (backgroundColorId == 0)
                setBackgroundColor(ContextCompat.getColor(context, R.color.purple_light))
            else if (backgroundColorId == 1)
                setBackgroundColor(ContextCompat.getColor(context, R.color.beige_light))

            // Subreddit icon
            val icon = styledAttributes.getDrawable(R.styleable.PostComponent_subreddit_icon)
            if (icon != null)
                findViewById<ShapeableImageView>(R.id.component_post_subreddit_icon).setImageDrawable(
                    icon
                )
            // Subreddit name
            findViewById<TextView>(R.id.component_post_text_subreddit).text =
                styledAttributes.getString(R.styleable.PostComponent_subreddit)
            // Sender name
            findViewById<TextView>(R.id.component_post_text_user).text =
                styledAttributes.getString(R.styleable.PostComponent_sender)
            // Post title
            findViewById<TextView>(R.id.component_post_text_title).text =
                styledAttributes.getString(R.styleable.PostComponent_post_title)

            // Post image (optional)
            val image = styledAttributes.getDrawable(R.styleable.PostComponent_image)
            val imageView = findViewById<ImageView>(R.id.component_post_image)
            if (image != null) {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(image)
            } else
                imageView.visibility = View.GONE

            // Post content (optional)
            val content = styledAttributes.getString(R.styleable.PostComponent_post_content)
            if (content != null) {
                postContentView.visibility = View.VISIBLE
                postContentView.text = content
            } else
                postContentView.visibility = View.GONE

            // Post votes count
            findViewById<TextView>(R.id.component_post_text_votes).text =
                styledAttributes.getInt(R.styleable.PostComponent_votes_count, 0).toString()
            // Post comment count
            findViewById<TextView>(R.id.component_post_text_comments).text =
                styledAttributes.getInt(R.styleable.PostComponent_comments_count, 0).toString()

            findViewById<ImageButton>(R.id.component_post_button_upvote).setOnClickListener(
                createButtonClickListener {
                    println(
                        "Up Vote clicked"
                    )
                }
            )
            findViewById<ImageButton>(R.id.component_post_button_down_vote).setOnClickListener(
                createButtonClickListener {
                    println(
                        "Down Vote clicked"
                    )
                }
            )
            findViewById<ImageButton>(R.id.component_post_button_comment).setOnClickListener(
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
            var anim = AnimationUtils.loadAnimation(this.context, R.anim.button_click)

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
