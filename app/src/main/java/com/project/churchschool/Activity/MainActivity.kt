package com.project.churchschool.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.Fragment.HomeFragment
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_drawer_header.*
import kotlinx.android.synthetic.main.main_drawer_header.view.*


class MainActivity : BasicActivity() {

    var backKeyPressedTime: Long = 0
    var navigationView : NavigationView? = null
    var headerView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        setBottomNavBar()
        setView(1)

        navigationView = findViewById<NavigationView>(R.id.main_navigationView)
        headerView = navigationView!!.getHeaderView(0)
        setDrawer(headerView!!)
    }

    override fun onResume() {
        super.onResume()
        updateDrawer(headerView!!)
    }

    fun findThisFragment(): Fragment? {
        var thisFrag : Fragment? = null
        for(fragment: Fragment in supportFragmentManager.fragments) {
            if (fragment.isVisible) {
                thisFrag = fragment
            }
        }
        return thisFrag
    }


    override fun onBackPressed() {

//        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount == 1){
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish()
            }
        }else{
            supportFragmentManager.popBackStack()

        }

    }

    private fun updateBottomMenu(navigation: NavigationView?) {
//        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("home")
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag("attendance")
//        val tag3: Fragment? = supportFragmentManager.findFragmentByTag("content")
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag("memo")
        val tag5: Fragment? = supportFragmentManager.findFragmentByTag("prayer")

//        if(tag1 != null && tag1.isVisible) {navigation.menu.findItem(R.id.Home).isChecked = true }
        if(tag2 != null && tag2.isVisible) {navigation?.menu?.findItem(R.id.AttendanceCheck)?.isChecked = true }
//        if(tag3 != null && tag3.isVisible) {navigation.menu.findItem(R.id.Contents).isChecked = true }
        if(tag4 != null && tag4.isVisible) {navigation?.menu?.findItem(R.id.Memo)?.isChecked = true }
        if(tag5 != null && tag5.isVisible) {navigation?.menu?.findItem(R.id.PrayerList)?.isChecked = true }

    }


}