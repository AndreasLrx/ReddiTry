package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.redditry.R
import com.redditry.databinding.ComponentListSearchBinding

class SearchComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
    
) : LinearLayout(context, attrs, defStyleAttr) {
    var binding: ComponentListSearchBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.component_list_search, this, true)
        binding = ComponentListSearchBinding.bind(this)
    }
}
