package com.redditry.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.ListView
import androidx.drawerlayout.widget.DrawerLayout
import com.redditry.R
import com.redditry.databinding.ComponentMainDrawerBinding

class MainDrawerComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var listView: ListView
    private var arrayList: ArrayList<DrawerItemData> = ArrayList()
    private var adapter: AdapterDrawerItem? = null
    private var binding: ComponentMainDrawerBinding

    lateinit var drawer: DrawerLayout

    init {
        binding = ComponentMainDrawerBinding.inflate(LayoutInflater.from(context), this)

        listView = binding.subredditList
        // Placeholders here
        arrayList.add(Pair("r/Mashu", R.drawable.redditry_logo))
        arrayList.add(Pair("r/Azhar", R.drawable.baseline_menu_24))
        arrayList.add(Pair("r/Niyaz", R.drawable.baseline_search_24))
        adapter = AdapterDrawerItem(context, arrayList)
        listView.adapter = adapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ -> drawer.close() }

        binding.sortBySpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    drawer.close()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }
}
