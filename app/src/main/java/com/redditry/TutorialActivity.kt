package com.redditry

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.gtappdevelopers.kotlingfgproject.ViewPagerAdapter

class TutorialActivity : ActivityHead() {

    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var imageList: List<Int>

    // on create set the view pager adapter and set the listener to change the activity when the last page is reached
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        viewPager = findViewById(R.id.idViewPager)

        // Add all the images to the list
        imageList = ArrayList<Int>()
        imageList = imageList + R.drawable.tutorial1
        imageList = imageList + R.drawable.tutorial2
        imageList = imageList + R.drawable.tutorial3
        imageList = imageList + R.drawable.tutorial4
        imageList = imageList + R.drawable.tutorial5
        imageList = imageList + R.drawable.tutorial6
        imageList = imageList + R.drawable.tutorial7
        imageList = imageList + R.drawable.tutorial8
        imageList = imageList + R.drawable.tutorial9
        imageList = imageList + R.drawable.tutorial10

        viewPagerAdapter = ViewPagerAdapter(this@TutorialActivity, imageList)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            // When the last page is reached, change the activity to the main activity
            override fun onPageSelected(position: Int) {
                if (position == 9) {
                    val intent = Intent(this@TutorialActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
        viewPager.adapter = viewPagerAdapter
    }
}
