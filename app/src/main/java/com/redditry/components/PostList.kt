package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import com.redditry.LazyLoader
import com.redditry.R
import com.redditry.databinding.ComponentPostListBinding
import com.redditry.redditAPI.PostData


class PostList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var binding: ComponentPostListBinding
    private var listView: ListView
    val adapter: AdapterPostList = AdapterPostList(context, ArrayList())

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
        listView.addFooterView(ProgressBar(context))
        listView.setOnScrollListener(object : LazyLoader() {
            override fun loadMore(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                Thread {
                    // Replace with api call
                    for (i in 0..9) {
                        val post =
                            PostData(
                                "title " + (i + totalItemCount - 1).toString(),
                                "this is a post content\na\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "a\n",
                                "r/test",
                                42,
                                62,
                                21,
                                "u/me",
                                null
                            )

                        adapter.postData.add(post)
                    }
                    adapter.loadPosts(totalItemCount - 1)
                }.start()
            }
        })
    }
}
