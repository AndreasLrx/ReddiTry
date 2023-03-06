package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.redditry.R
import com.redditry.databinding.ComponentPostProfileBinding
import com.redditry.redditAPI.PostData

class ProfilePosts @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ComponentPostProfileBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.component_post_profile, this, true)
        binding = ComponentPostProfileBinding.bind(this)
    }

    fun setPost(array: Array<PostData>) {
        var postList: MutableList<Post> = ArrayList()

        array.forEach {
            val post = Post(context, null)
            post.setData(it)
            postList.add(post)
        }

        postList.forEach {
            binding.postContainer.addView(it)
        }
    }
}
