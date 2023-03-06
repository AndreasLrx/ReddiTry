package com.redditry.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.redditry.R
import com.redditry.databinding.ComponentSubredditListItemBinding
import com.redditry.redditAPI.Subreddit

class SubredditListItem constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var binding: ComponentSubredditListItemBinding
    private var _icon: Drawable? = null
    private var _title: String? = null

    private var icon: Drawable?
        get() = _icon
        set(value) {
            _icon = value
            binding.icon.setImageDrawable(icon)
        }
    private var title: String?
        get() = _title
        set(value) {
            _title = value
            binding.text.text = title
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_subreddit_list_item, this, true)
        binding = ComponentSubredditListItemBinding.bind(this)
    }

    fun setData(data: Subreddit) {
        title = data.displayName
        if (data.icon_img != null)
            Glide.with(this)
                .asDrawable()
                .load(data.icon_img)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in Drawable?>?
                    ) {
                        icon = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
    }
}
