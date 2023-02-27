package com.redditry.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.imageview.ShapeableImageView
import com.redditry.R

typealias DrawerItemData = Pair<String, Int>


class AdapterDrawerItem(
    private val context: Context,
    private val arrayList: java.util.ArrayList<DrawerItemData>
) : BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var adaptedView =
            LayoutInflater.from(context).inflate(R.layout.adapter_drawer_item, parent, false)
        adaptedView.findViewById<TextView>(R.id.adapter_drawer_text).text =
            arrayList[position].first
        adaptedView.findViewById<ShapeableImageView>(R.id.adapter_drawer_icon)
            .setImageDrawable(AppCompatResources.getDrawable(context, arrayList[position].second))
        return adaptedView
    }
}