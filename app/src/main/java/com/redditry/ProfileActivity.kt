package com.redditry

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.redditry.controller.Post
import com.redditry.controller.User
import com.redditry.databinding.ActivityProfileBinding
import com.redditry.redditAPI.Pref
import java.util.Locale

class ProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileBinding
    private val user = User()
    private var username: String? = null

    // On create inflate the layout and set the edit button to open the edit profile activity
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBar.selectedItemId = R.id.profil_icon
        navBar = binding.navBar

        // ActivityHead setup
        navigationId = R.id.profil_icon
        navBar = binding.navBar

        val extras = intent.extras
        if (extras != null) {
            username = extras.getString("user_name")
            if (username != null)
                binding.header.visibility = View.GONE
        }
        var subredditId = ""
        var profilId = ""

        // on click on the edit button open the edit profile activity
        binding.header.binding.editButton.setOnClickListener {
            openActivity(
                EditProfileActivity::class.java,
                0, // Intent.FLAG_ACTIVITY_NO_HISTORY,
                false,
                fun(intent: Intent) {
                    intent.putExtra(
                        "username",
                        binding.description.getUsername()
                    ); intent.putExtra(
                        "description",
                        binding.description.getDescription()
                    ); intent.putExtra(
                        "underage",
                        binding.description.getUnderage()
                    ); intent.putExtra(
                        "country",
                        binding.description.getCountry()
                    ); intent.putExtra("id", subredditId)
                    intent.putExtra("profilId", profilId)
                }
            )
        }
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

        binding.posts.binding.postList.binding.sortBySpinner.visibility = View.GONE

        // get the user profile and set the view
        Thread {
            val profile = if (username == null)
                user.getMyProfil()
            else
                user.getUser(username!!)

            var pref: Pref? = null
            if (username == null) {
                pref = user.getPref()
            }

            username = profile?.name

            subredditId = profile?.subreddit?.name.toString()
            profilId = profile?.subreddit?.displayNameId.toString()

            // set the view
            runOnUiThread {
                binding.description.setUsername(
                    (if (profile?.subreddit?.title != "") profile?.subreddit?.title else profile.name)
                        ?: "Username"
                )
                binding.description.setDescription(profile?.subreddit?.description.toString())
                binding.description.setFollowersNumber(profile?.subreddit?.subscribers.toString())
                binding.description.setUnderage(profile?.over_18 == true)
                if (pref != null) {
                    val loc = Locale(
                        "",
                        pref.country_code
                    )

                    binding.description.setCountry(loc.displayCountry)
                } else {
                    binding.description.binding.profileCountry.visibility = View.GONE
                }
                profile?.icon_img?.let { binding.bannerAndPp.setImage(it) }
                profile?.subreddit?.banner_img?.let { binding.bannerAndPp.setBanner(it) }
                if (profile != null) {
                    if (!profile.subreddit.isAcceptingFollowers) {
                        binding.description.desactivateButton()
                    }
                }
            }

            // set the lazy loading for the post list
            binding.posts.binding.postList.setLazyLoading(
                fun(
                    _: Post.SortBy,
                    after: String
                ): Pair<ArrayList<com.redditry.redditAPI.Post>, String?> {
                    return if (username == null)
                        Pair(ArrayList(), null)
                    else
                        user.getMyPost(username!!, after)
                }
            )
        }.start()
    }
}
