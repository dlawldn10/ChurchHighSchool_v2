package com.project.churchschool.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.churchschool.R


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)


        rootView.findViewById<ImageView>(R.id.CardNewsView_imageView).setOnClickListener {
            Toast.makeText(
                context, "카드뉴스 화면 클릭.",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/channel/UCGpzqybTJf70PySoTQAicJw"))

            startActivity(intent)
        }
        return rootView
    }


}