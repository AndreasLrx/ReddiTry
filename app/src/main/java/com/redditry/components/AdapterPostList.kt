package com.redditry.components

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.redditry.redditAPI.PostData

class AdapterPostList(
    private val context: Context,
    posts: ArrayList<PostData>
) : BaseAdapter() {
    enum class Color {
        Alternate, HalfTransparent
    }

    private lateinit var postViews: ArrayList<Post>
    private lateinit var _postData: ArrayList<PostData>
    private var _color: Color = Color.Alternate

    var postData: ArrayList<PostData>
        get() = _postData
        set(value) {
            _postData = value
            postViews = ArrayList()
            loadPosts()
        }
    var color: Color
        get() = _color
        set(value) {
            _color = value
            var index = 0
            postViews.forEach {
                it.backgroundColor = postColorAt(index++)
            }
        }

    init {
        postData = posts
    }

    fun reload() {
        // Triggers the setter
        postData = postData
    }

    fun loadPosts(start: Int = 0) {
        if (postViews.size < start)
            throw java.lang.IndexOutOfBoundsException("Try to load posts from " + start.toString() + " where list have only " + postViews.size + " elements.")

        postViews.ensureCapacity(postData.size)
        for (i in start until postData.size) {
            val post = Post(context, null)
            post.setData(postData[i])
            post.backgroundColor = postColorAt(i)
            if (i >= postViews.size)
                postViews.add(post)
            else
                postViews[i] = post
        }
        val mainHandler = Handler(context.mainLooper)
        mainHandler.post { notifyDataSetChanged() }
    }

    fun addPosts(posts: ArrayList<PostData>) {
        var start = postData.size
        postData.addAll(posts)
        loadPosts(start)
    }

    private fun postColorAt(position: Int): Post.Color {
        return if (color == Color.Alternate) {
            if (position % 2 == 0)
                Post.Color.Purple
            else
                Post.Color.Orange
        } else
            Post.Color.HalfTransparent
    }

    override fun getCount(): Int {
        return postData.size
    }

    override fun getItem(position: Int): Any {
        return postData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return postViews[position]
    }
}