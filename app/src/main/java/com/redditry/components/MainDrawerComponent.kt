package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ListView
import com.redditry.R


class MainDrawerComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var listView: ListView
    private var arrayList: ArrayList<DrawerItemData> = ArrayList()
    private var adapter: AdapterDrawerItem? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.component_main_drawer, this, true)
        listView = findViewById<ListView>(R.id.component_main_drawer_list_subreddit);
        // Placeholders here
        arrayList.add(Pair("r/Mashu", R.drawable.redditry_logo))
        arrayList.add(Pair("r/Azhar", R.drawable.baseline_menu_24))
        arrayList.add(Pair("r/Niyaz", R.drawable.baseline_search_24))
        adapter = AdapterDrawerItem(context, arrayList)
        listView.adapter = adapter
    }
}
