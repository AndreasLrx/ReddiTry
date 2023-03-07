package com.redditry.components

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.redditry.LazyLoader
import com.redditry.R
import com.redditry.databinding.ComponentPostListBinding
import java.io.InterruptedIOException

typealias OnLoad = (sortBy: com.redditry.controller.Post.SortBy, after: String) -> Pair<ArrayList<com.redditry.redditAPI.Post>, String?>

class PostList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    var binding: ComponentPostListBinding
    var listView: ListView
    val adapter: AdapterPostList = AdapterPostList(context, ArrayList())
    private var progressBar: ProgressBar? = null

    // Lazy loading
    var lazyLoader: LazyLoader? = null
    private var after: String? = ""
    private var loadingThread: Thread? = null
    private var sortBy: com.redditry.controller.Post.SortBy =
        com.redditry.controller.Post.SortBy.values()[0]

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
        if (progressBar != null) {
            listView.removeFooterView(progressBar)
            progressBar = null
        } else {
            progressBar = ProgressBar(context)
            listView.addFooterView(progressBar)
        }
    }

    fun setProgressBar(visible: Boolean = true) {
        if ((visible && progressBar == null) || (!visible && progressBar != null))
            toggleProgressBar()
    }

    fun setLazyLoading(loadingFct: OnLoad) {
        setProgressBar(true)

        lazyLoader = object : LazyLoader() {
            override fun loadMore(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                loadingThread =
                    Thread {
                        try {
                            if (after != null) {
                                val res =
                                    loadingFct(sortBy, after!!) // posts.getPosts(sortBy, after)
                                if (res.first.size != 0)
                                    adapter.addPosts(res.first)
                                after = res.second
                                // No more posts
                                if (after == null)
                                    Handler(context.mainLooper).post { setProgressBar(false) }
                                lazyLoader?.notifyEndLoading()
                            }
                        } catch (_: InterruptedIOException) {
                        }
                    }
                loadingThread!!.start()
            }
        }
        listView.setOnScrollListener(lazyLoader)

        binding.sortBySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == sortBy.ordinal)
                    return
                if (loadingThread != null) {
                    loadingThread!!.interrupt()
                    loadingThread = null
                }
                lazyLoader?.notifyEndLoading()
                sortBy = com.redditry.controller.Post.SortBy.values()[p2]
                after = ""
                invalidate()
                adapter.clear()
                setProgressBar(true)
                // Force reload
                listView.scrollBy(0, 0)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}
