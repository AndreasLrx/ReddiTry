package com.redditry.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.redditry.R
import com.redditry.databinding.ComponentPostBinding

class Post @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    enum class Format {
        Expanded, Small
    }

    enum class Color {
        Purple, Orange, HalfTransparent
    }

    private var binding: ComponentPostBinding
    private var _format: Format = Format.Expanded
    private var _backgroundColor: Color = Color.Purple
    private var _icon: Drawable? = null
    private var _subredditName: String? = null
    private var _userName: String? = null
    private var _title: String? = null
    private var _image: Drawable? = null
    private var _content: String? = null
    private var _votes: Int = 0
    private var _comments: Int = 0

    var format: Format
        get() = _format
        set(value) {
            _format = value
            if (_format == Format.Small) {
                if (image != null)
                    binding.content.maxLines = 3
                else
                    binding.content.maxLines = 8
            } else
                binding.content.maxLines = -1
        }
    var backgroundColor: Color
        get() = _backgroundColor
        set(value) {
            _backgroundColor = value
            when (_backgroundColor) {
                Color.Purple -> setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.purple_light
                    )
                )
                Color.Orange -> setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.beige_light
                    )
                )
                Color.HalfTransparent -> setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.semi_transparent
                    )
                )
            }
        }
    var icon: Drawable?
        get() = _icon
        set(value) {
            _icon = value
            binding.subredditIcon.setImageDrawable(_icon)
        }
    var subredditName: String?
        get() = _subredditName
        set(value) {
            _subredditName = if (value == "")
                null
            else
                value
            binding.subredditName.text = _subredditName
        }
    var userName: String?
        get() = _userName
        set(value) {
            _userName = value
            binding.userName.text = _userName
        }
    var title: String?
        get() = _title
        set(value) {
            _title = value
            binding.title.text = _title
        }
    var image: Drawable?
        get() = _image
        set(value) {
            _image = value
            // update format bc it depends on image
            format = format
            if (image != null) {
                binding.image.visibility = View.VISIBLE
                binding.image.setImageDrawable(image)
            } else
                binding.image.visibility = View.GONE
        }
    var content: String?
        get() = _content
        set(value) {
            _content = if (value == "")
                null
            else
                value
            if (content != null) {
                binding.content.visibility = View.VISIBLE
                binding.content.text = content
            } else
                binding.content.visibility = View.GONE
        }
    var votes: Int
        get() = _votes
        set(value) {
            _votes = value
            binding.votes.text = votes.toString()
        }
    var comments: Int
        get() = _comments
        set(value) {
            _comments = value
            binding.comments.text = comments.toString()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_post, this, true)
        binding = ComponentPostBinding.bind(this)

        attrs?.let {

            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.Post, 0, 0)

            // Post format
            format = Format.values()[styledAttributes.getInt(R.styleable.Post_post_format, 0)]
            // Background Color
            backgroundColor =
                Color.values()[styledAttributes.getInt(R.styleable.Post_post_color, 0)]
            // Subreddit icon
            icon = styledAttributes.getDrawable(R.styleable.Post_subreddit_icon)
            // Subreddit name
            subredditName = styledAttributes.getString(R.styleable.Post_subreddit)
            // Sender name
            userName = styledAttributes.getString(R.styleable.Post_sender)
            // Post title
            title = styledAttributes.getString(R.styleable.Post_post_title)
            // Post image (optional)
            image = styledAttributes.getDrawable(R.styleable.Post_image)
            // Post content (optional)
            content = styledAttributes.getString(R.styleable.Post_post_content)
            // Post votes count
            votes = styledAttributes.getInt(R.styleable.Post_votes_count, 0)
            // Post comment count
            comments = styledAttributes.getInt(R.styleable.Post_comments_count, 0)
            styledAttributes.recycle()
        }

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
    }

    fun setData(data: com.redditry.redditAPI.Post) {
        subredditName = data.subreddit_name_prefixed
        title = data.title
        content = data.content
        votes = data.voteUp - data.voteDown
        comments = data.numComments
        userName = "u/" + data.author
        backgroundColor = backgroundColor
        format = Format.Small
        if (data.imageUrl != null)
            Glide.with(this)
                .asDrawable()
                .load(data.imageUrl)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in Drawable?>?
                    ) {
                        image = resource
                    }

                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                })

        /*
        format
        image
        icon
         */
    }

    fun toggleExpand() {
        format = if (format == Format.Expanded)
            Format.Small
        else
            Format.Expanded
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
