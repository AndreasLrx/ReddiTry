package com.redditry.components

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.redditry.redditAPI.Subreddit

class AdapterSubredditList(
    private val context: Context,
    subreddits: ArrayList<Subreddit> = ArrayList()
) : BaseAdapter() {
    private lateinit var views: ArrayList<SubredditListItem>
    private lateinit var _data: ArrayList<Subreddit>
    var data: ArrayList<Subreddit>
        get() = _data
        set(value) {
            _data = value
            views = ArrayList()
            loadViews()
        }

    init {
        data = subreddits
    }

    private fun loadViews() {
        for (i in 0 until data.size) {
            val view = SubredditListItem(context, null)
            view.setData(data[i])
            views.add(view)
        }
        val mainHandler = Handler(context.mainLooper)
        mainHandler.post { notifyDataSetChanged() }
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return views[position]
    }
}
