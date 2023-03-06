package com.redditry.components

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.ListView
import androidx.drawerlayout.widget.DrawerLayout
import com.redditry.SubredditActivity
import com.redditry.databinding.ComponentMainDrawerBinding

class MainDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var listView: ListView
    var adapter: AdapterSubredditList = AdapterSubredditList(context)
    var binding: ComponentMainDrawerBinding

    lateinit var drawer: DrawerLayout

    init {
        binding = ComponentMainDrawerBinding.inflate(LayoutInflater.from(context), this)

        listView = binding.subredditList
        // Placeholders here
        listView.adapter = adapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, i, _ ->
                drawer.close()
                val intent = Intent(view.context, SubredditActivity::class.java)
                intent.putExtra("subreddit_name", adapter.data[i].displayName)
                view.context.startActivity(intent)
            }

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
