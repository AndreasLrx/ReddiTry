package com.redditry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.redditry.controller.Post
import com.redditry.controller.Subreddit
import com.redditry.databinding.ActivitySubredditBinding

class SubredditActivity : ActivityHead() {
    private lateinit var binding: ActivitySubredditBinding
    private val subreddit: Subreddit = Subreddit()
    private val posts: Post = Post()

    private lateinit var _name: String
    private var _title: String = ""
    private var _members: Int = 0
    private var id = ""
    private var isSubscriber: Boolean = false

    private var name: String
        get() = _name
        set(value) {
            _name = value
            binding.description.binding.subredditName.text = name
        }
    private var title: String
        get() = _title
        set(value) {
            _title = value
            binding.description.binding.subredditTitle.text = title
        }
    private var members: Int
        get() = _members
        set(value) {
            _members = value
            binding.description.binding.members.text =
                String.format(resources.getString(R.string.members), members)
        }

    // on create inflate the layout
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubredditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationId = R.id.menu_icon
        navBar = binding.navBar

        // on touch on the post list disable the scroll view to scroll
        binding.posts.binding.postList.listView.setOnTouchListener(
            View.OnTouchListener { _, event ->
                binding.scrollView.requestDisallowInterceptTouchEvent(true)
                when (event.actionMasked) {
                    MotionEvent.ACTION_UP -> binding.scrollView.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
                false
            }
        )

        val extras = intent.extras
        // get the subreddit name from the intent
        if (extras != null) {
            name = extras.getString("subreddit_name", "r/Android")

            binding.description.binding.subscribeButton.setOnClickListener {

                // subscribe or unsubscribe to the subreddit
                Thread {

                    val srFullname = "t5_$id"

                    if (isSubscriber) {
                        val result = subreddit.unsubscribe(srFullname)
                        if (result.isSuccessful) {
                            runOnUiThread {
                                binding.description.binding.subscribeButton.text = "Subscribe"
                            }
                        } else {
                            runOnUiThread {
                                binding.description.binding.subscribeButton.text = "Error"
                            }
                        }
                    } else {
                        val result = subreddit.subscribe(srFullname)
                        if (result.isSuccessful) {
                            runOnUiThread {
                                binding.description.binding.subscribeButton.text = "Unsubscribe"
                            }
                        } else {
                            runOnUiThread {
                                binding.description.binding.subscribeButton.text = "Error"
                            }
                        }
                    }
                }.start()
            }

            // get the subreddit info
            Thread {
                val res = subreddit.getSubreddit(name) ?: return@Thread

                id = res.id ?: ""
                isSubscriber = res.isSubscriber == true

                // set the info
                runOnUiThread {
                    title = res.title
                    members = res.subscribers
                    binding.description.binding.description.text = res.description
                    binding.description.binding.subscribeButton.text =
                        if (res.isSubscriber == true) "Unsubscribe" else "Subscribe"
                    res.icon_img?.let { binding.bannerAndPp.setImage(it) }
                    res.mobile_banner_img?.let { binding.bannerAndPp.setBanner(it) }
                }
            }.start()

            // set the lasy loading function for the post list
            binding.posts.binding.postList.setLazyLoading(
                fun(
                    sortBy: Post.SortBy,
                    after: String
                ): Pair<ArrayList<com.redditry.redditAPI.Post>, String?> {
                    return posts.getPostsFrom(title, sortBy, after, 10)
                }
            )
        }
    }
}
