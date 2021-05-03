package com.project.churchschool.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.*
import com.project.churchschool.R

class PrayerPostFragment : Fragment() {


    val database = Firebase.database
    val myRef = database.getReference("prayers")
    var prayer : PrayerData = PrayerData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_prayer_post, container, false)
        val prayerText = rootView.findViewById<EditText>(R.id.Prayer_text_EditText)

        rootView.findViewById<ExtendedFloatingActionButton>(R.id.Prayer_Save_Bttn).setOnClickListener {

            var text : String = prayerText.text.toString()

            if(text != null){
                prayer.text = text
                prayer.date = (activity as BasicActivity).getYYYYMMDD()
                myRef.push().setValue(prayer) //푸시를 해야 데이터가 묶음으로 들어감
            }
            (activity as BasicActivity).setView(5)
        }

        return rootView
    }



}

