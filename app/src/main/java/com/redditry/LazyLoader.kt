package com.redditry

import android.widget.AbsListView

abstract class LazyLoader constructor() : AbsListView.OnScrollListener {
    @Volatile
    private var loading = false
    private var threshold = 1

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
    override fun onScroll(
        view: AbsListView,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {
        // check if the List needs more data
        if (!loading && (firstVisibleItem + visibleItemCount) >= (totalItemCount - threshold)) {
            loading = true
            // List needs more data. Go fetch !!
            loadMore(
                view, firstVisibleItem,
                visibleItemCount, totalItemCount
            )
        }
    }

    fun notifyEndLoading() {
        loading = false
    }

    // Called when the user is nearing the end of the ListView
    // and the ListView is ready to add more items.
    abstract fun loadMore(
        view: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    )
}
