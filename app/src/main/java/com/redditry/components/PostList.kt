package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import com.redditry.LazyLoader
import com.redditry.R
import com.redditry.databinding.ComponentPostListBinding

typealias OnLoad = (adapter: AdapterPostList, start: Int) -> Unit

class PostList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    var binding: ComponentPostListBinding
    var listView: ListView
    val adapter: AdapterPostList = AdapterPostList(context, ArrayList())
    private var _onLoad: OnLoad? = null
    var onLoad: OnLoad?
        get() = _onLoad
        set(value) {
            _onLoad = value
        }
    private var progressBar: ProgressBar? = null
    var lazyLoader: LazyLoader? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.component_post_list, this, true)
        binding = ComponentPostListBinding.bind(this)

        listView = binding.postList
        listView.adapter = adapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, _, _ ->
                if (view is Post)
                    view.toggleExpand()
            }
    }

    fun toggleProgressBar() {
        if (progressBar != null)
            listView.removeFooterView(progressBar)
        else {
            progressBar = ProgressBar(context)
            listView.addFooterView(progressBar)
        }
    }

    fun setProgressBar(visible: Boolean = true) {
        if ((visible && progressBar == null) || (!visible && progressBar != null))
            toggleProgressBar()
    }

    fun setLazyLoading(loadingFct: OnLoad? = null) {
        toggleProgressBar()

        if (loadingFct != null)
            onLoad = loadingFct
        else
            onLoad = fun(adapter, start) {
                Thread {
                    // Replace with api call
                    for (i in 0..9) {
                        val post =
                            com.redditry.redditAPI.Post(
                                "title " + (i + start).toString(),
                                "this is a post content\na\na\na\na\na\na\na\na\na\na\na\na",
                                "r/test",
                                42,
                                62,
                                21,
                                "u/me",
                                null
                            )
                        adapter.postData.add(post)
                    }
                    adapter.loadPosts(start)
                }.start()
            }
        lazyLoader = object : LazyLoader() {
            override fun loadMore(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (onLoad != null)
                    onLoad?.invoke(adapter, totalItemCount - 1)
            }
        }
        listView.setOnScrollListener(lazyLoader)
    }
}
