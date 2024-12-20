package com.scwang.wave.app.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scwang.wave.app.R
import com.scwang.wave.app.fragment.WaveConsoleFragment
//import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private val mNavigation by lazy { findViewById<BottomNavigationView>(R.id.navigation) }
    private val mContent by lazy { findViewById<FrameLayout>(R.id.content) }
    private val mMessage by lazy { findViewById<TextView>(R.id.message) }
    private val mContainer by lazy { findViewById<ConstraintLayout>(R.id.container) }

    enum class Tabs(val menuId: Int, val clazz: KClass<out androidx.fragment.app.Fragment>) {
        WavePair(R.id.navigation_home, WaveConsoleFragment::class),
        Wave(R.id.navigation_dashboard, WaveConsoleFragment::class),
        Wave2(R.id.navigation_notifications, WaveConsoleFragment::class),
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mNavigation.setOnNavigationItemSelectedListener(this)
        mNavigation.selectedItemId = R.id.navigation_home
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        supportFragmentManager
                .beginTransaction()
                .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content, Tabs.values().first { it.menuId==item.itemId }.clazz.java.newInstance())
                .commit()
        return true
    }

}
