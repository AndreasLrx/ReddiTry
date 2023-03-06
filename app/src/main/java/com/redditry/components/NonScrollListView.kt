package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ListView

class NonScrollListView constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ListView(context, attrs) {
    var scrollable: Boolean = true

    init {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxHeight = if (!scrollable) MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST
        ) else heightMeasureSpec
        super.onMeasure(widthMeasureSpec, maxHeight)
        if (!scrollable) {
            val params: ViewGroup.LayoutParams = layoutParams
            params.height = measuredHeight
        }
    }
}
