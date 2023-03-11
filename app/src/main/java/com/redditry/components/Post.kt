package com.redditry.components

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.MediaController
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.redditry.ProfileActivity
import com.redditry.R
import com.redditry.SubredditActivity
import com.redditry.databinding.ComponentPostBinding
import com.redditry.controller.Post as PostController

class Post
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    enum class Format {
        Expanded,
        Small
    }

    enum class Color {
        Purple,
        Orange,
        HalfTransparent
    }

    private val postController: PostController = PostController()
    private var binding: ComponentPostBinding
    private var _format: Format = Format.Expanded
    private var _backgroundColor: Color = Color.Purple
    private var _icon: Drawable? = null
    private var _subredditName: String? = null
    private var _userName: String? = null
    private var _title: String? = null
    private var _image: Drawable? = null
    private var _videoUrl: String? = null
    private var _content: String? = null
    private var _votes: Int = 0
    private var _comments: Int = 0
    private var _name = ""
    private var _voteButton: Boolean? = null

    var name: String
        get() = _name
        set(value) {
            _name = value
        }

    var format: Format
        get() = _format
        set(value) {
            _format = value
            if (_format == Format.Small) {
                if (image != null) binding.content.maxLines = 3 else binding.content.maxLines = 8
            } else binding.content.maxLines = Int.MAX_VALUE
        }
    var backgroundColor: Color
        get() = _backgroundColor
        set(value) {
            _backgroundColor = value
            when (_backgroundColor) {
                Color.Purple ->
                    setBackgroundColor(ContextCompat.getColor(context, R.color.purple_light))
                Color.Orange ->
                    setBackgroundColor(ContextCompat.getColor(context, R.color.beige_light))
                Color.HalfTransparent ->
                    setBackgroundColor(
                        ContextCompat.getColor(context, R.color.semi_transparent)
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
            _subredditName = if (value == "") null else value
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
                binding.video.visibility = View.GONE
                binding.image.setImageDrawable(image)
            } else binding.image.visibility = View.GONE
        }
    var videoUrl: String?
        get() = _videoUrl
        set(value) {
            _videoUrl = value
            if (videoUrl != null) {

                val uri = Uri.parse(videoUrl)
                if (uri != null) {
                    binding.image.visibility = View.GONE
                    binding.video.visibility = View.VISIBLE
                    binding.video.setVideoURI(uri)
                }
            } else {
                binding.video.visibility = View.GONE
            }
        }
    var content: String?
        get() = _content
        set(value) {
            _content = if (value == "") null else value
            if (content != null) {
                binding.content.visibility = View.VISIBLE
                binding.content.text = content
            } else binding.content.visibility = View.GONE
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
    var voteButton: Boolean?
        get() = _voteButton
        set(value) {
            _voteButton = value
            if (_voteButton == false) {
                binding.downVoteButton.foregroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.purple)
                    )
            } else if (_voteButton == true) {
                binding.upvoteButton.foregroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.orange)
                    )
            }
        }

    // inflate the post component layout and bind it to the binding variable
    init {
        LayoutInflater.from(context).inflate(R.layout.component_post, this, true)
        binding = ComponentPostBinding.bind(this)

        // initialize the media controller for the video
        // start and loop the video when it's prepared
        // set the video layout params
        val mediaController = MediaController(context)
        mediaController.setAnchorView(binding.video)
        binding.video.setMediaController(mediaController)
        binding.video.setOnPreparedListener {
            it.isLooping = true
            binding.video.start()
            binding.video.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            binding.video.invalidate()
        }

        binding.contentLayout.setOnClickListener { toggleExpand() }
        // set the click listeners for the subreddit and user name
        // open the subreddit activity selected by the user
        binding.subredditName.setOnClickListener {
            val intent = Intent(context, SubredditActivity::class.java)
            intent.putExtra("subreddit_name", binding.subredditName.text)
            context.startActivity(intent)
        }

        // open the user activity selected by the user
        binding.userName.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            // User name is without 'u/'
            intent.putExtra("user_name", binding.userName.text.substring(2))
            context.startActivity(intent)
        }

        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.Post, 0, 0)

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
        // set the click listeners for the upvote and downvote buttons
        binding.upvoteButton.setOnClickListener {
            Thread {
                val res = postController.vote(name, 1)
                val mainHandler = Handler(context.mainLooper)
                // update the UI in the main thread
                // if the vote was successful, update the votes count
                // if not, print the error code
                // change the upvote button color to purple
                if (res.isSuccessful) {
                    mainHandler.post {
                        votes++
                        binding.votes.text = votes.toString()
                        binding.upvoteButton.foregroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(context, R.color.purple)
                            )
                    }
                } else {
                    println("Error: ${res.code()}")
                }
            }
                .start()
        }
        binding.downVoteButton.setOnClickListener {
            Thread {
                val res = postController.vote(name, -1)
                val mainHandler = Handler(context.mainLooper)

                if (res.isSuccessful) {
                    mainHandler.post {
                        votes--
                        binding.votes.text = votes.toString()
                        binding.downVoteButton.foregroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(context, R.color.purple)
                            )
                    }
                } else {
                    println("Error: ${res.code()}")
                }
            }
                .start()
        }
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
        name = data.name
        voteButton = data.likes

        var imageUrl: String? = null
        // check if the post is a gallery or a video
        // if it is, get the image/video url
        // if not, get the image url
        if (data.contentUrl != null) {
            if (data.is_gallery == true) {
                val id = data.gallery_data?.items?.get(0)?.media_id
                val imageType = id?.let { data.media_metadata?.getJSONObject(it)?.getString("m") }
                imageUrl = "https://i.redd.it/$id" + "." + imageType!!.substring(6)
            } else if (data.is_video == true) {
                videoUrl = data.media?.getJSONObject("reddit_video")?.getString("fallback_url")
            } else {
                imageUrl = data.contentUrl
            }
            if (imageUrl != null)
                Glide.with(this)
                    .asDrawable()
                    .load(imageUrl)
                    .into(
                        object : CustomTarget<Drawable?>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition:
                                    com.bumptech.glide.request.transition.Transition<
                                        in Drawable?>?
                            ) {
                                image = resource
                            }

                            override fun onLoadCleared(
                                @Nullable placeholder: Drawable?
                            ) {
                            }
                        }
                    )
        }
    }

    // set the post format
    fun toggleExpand() {
        format = if (format == Format.Expanded) Format.Small else Format.Expanded
    }

    private fun createButtonClickListener(onEndCallback: () -> Unit): View.OnClickListener {
        // loading animation
        return View.OnClickListener {
            val anim = AnimationUtils.loadAnimation(this.context, R.anim.button_click)

            anim.interpolator = AccelerateInterpolator(0.7f)
            it.startAnimation(anim)
            anim.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}

                    override fun onAnimationEnd(p0: Animation?) {
                        onEndCallback()
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}
                }
            )
        }
    }
}
