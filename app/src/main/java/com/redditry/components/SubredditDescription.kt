package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.redditry.R
import com.redditry.databinding.ComponentDescriptionSubredditBinding

class SubredditDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var binding: ComponentDescriptionSubredditBinding

    // inflate the component description subreddit layout
    init {
        LayoutInflater.from(context).inflate(R.layout.component_description_subreddit, this, true)
        binding = ComponentDescriptionSubredditBinding.bind(this)
    }
}
