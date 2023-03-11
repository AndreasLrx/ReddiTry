package com.gtappdevelopers.kotlingfgproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.redditry.R
import java.util.Objects

class ViewPagerAdapter(val context: Context, private val imageList: List<Int>) : PagerAdapter() {
    // return the size of the list.
    override fun getCount(): Int {
        return imageList.size
    }

    //  return the object
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    // initialize our item and inflating our layout file
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // initialize our layout inflater.
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //  inflate our custom layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)

        // initialize our view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView

        //  set image resource for image view.
        imageView.setImageResource(imageList[position])

        // add this item view to the container.
        Objects.requireNonNull(container).addView(itemView)

        // return our item view.
        return itemView
    }

    // destroy item method.
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // on below line we are removing view
        container.removeView(`object` as RelativeLayout)
    }

    // methode who call when the last slide is reached and the activity is finish
    override fun finishUpdate(container: ViewGroup) {
        super.finishUpdate(container)
    }
}
