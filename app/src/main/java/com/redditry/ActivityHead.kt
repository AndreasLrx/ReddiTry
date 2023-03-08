package com.redditry

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

open class ActivityHead : AppCompatActivity() {
    private lateinit var _navBar: BottomNavigationView
    protected var navBar: BottomNavigationView
        get() = _navBar
        set(value) {
            _navBar = value
            _navBar.setOnItemSelectedListener(onNavBarItemSelected)
        }

    protected var navigationId: Int? = null
    private val onNavBarItemSelected =
        object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (onNavBarItem(item))
                    return true
                if (item.itemId == navBar.selectedItemId || item.itemId == navigationId)
                    return true
                when (item.itemId) {
                    R.id.search_icon -> openActivity(
                        SearchActivity::class.java
                    )
                    R.id.redditry_icon -> openActivity(
                        MainActivity::class.java,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                    )
                    R.id.profil_icon -> openActivity(
                        ProfileActivity::class.java
                    )
                    else -> {
                        return false
                    }
                }
                return true
            }
        }

    override fun onResume() {
        super.onResume()
        if (navigationId != null)
            navBar.selectedItemId = navigationId!!
    }

    protected open fun onNavBarItem(item: MenuItem): Boolean {
        return false
    }

    fun openActivity(
        Activity: Class<*>,
        flags: Int = 0,
        animate: Boolean = false,
        beforeStart: ((Intent) -> Unit)? = null
    ) {
        val i = Intent(this, Activity)
        if (!animate)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        i.addFlags(flags)
        if (beforeStart != null)
            beforeStart(i)

        startActivity(i)
    }
}
