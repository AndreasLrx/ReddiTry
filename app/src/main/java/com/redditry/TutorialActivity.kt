package com.redditry

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.gtappdevelopers.kotlingfgproject.ViewPagerAdapter

class TutorialActivity : ActivityHead() {

    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var imageList: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)


        viewPager = findViewById(R.id.idViewPager)

        imageList = ArrayList<Int>()
        imageList = imageList + R.drawable.tutorial1
        imageList = imageList + R.drawable.tutorial2
        imageList = imageList + R.drawable.tutorial3
        imageList = imageList + R.drawable.tutorial4
        imageList = imageList + R.drawable.tutorial5
        imageList = imageList + R.drawable.tutorial6
        imageList = imageList + R.drawable.tutorial7
        imageList = imageList + R.drawable.tutorial8


        viewPagerAdapter = ViewPagerAdapter(this@TutorialActivity, imageList)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                if (position == 7){
                    val intent = Intent(this@TutorialActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
        viewPager.adapter = viewPagerAdapter
    }
}
