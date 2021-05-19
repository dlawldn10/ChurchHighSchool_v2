package com.project.churchschool.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
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


    override fun onBackPressed() {
//        super.onBackPressed()

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
        }
    }


}