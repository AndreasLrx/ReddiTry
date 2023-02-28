package com.redditry

import android.os.Bundle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.redditry.databinding.ActivityMainBinding

class MainActivity : ActivityHead() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawer = binding.root
        binding.drawerComponent.drawer = drawer

        // ActivityHead setup
        navigationId = R.id.redditry_icon
        navBar = binding.navBar
    }

    override fun onNavBarItem(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_icon) {
            drawer.open()
        }
        return super.onNavBarItem(item)
    }
}
