package com.redditry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redditry.components.MainDrawerComponent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var drawer = findViewById<DrawerLayout>(R.id.activity_main_drawer)
        findViewById<MainDrawerComponent>(R.id.activity_main_drawer_component).drawer = drawer

        findViewById<BottomNavigationView>(R.id.activity_main_navigation)
            .setOnItemSelectedListener { item ->
                if (item.itemId == R.id.menu_icon) drawer.open()
                true
            }
    }
}
