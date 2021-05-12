package com.project.churchschool.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("이 기도제목을 공유하시겠습니까?")
            builder.setMessage("한번 공유하면 삭제하거나 수정할 수 없습니다")
            builder.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
                var text : String = prayerText.text.toString()

                if(text != null){
                    prayer.text = text
                    prayer.date = (activity as BasicActivity).getYYYYMMDD()
                    myRef.push().setValue(prayer) //푸시를 해야 데이터가 묶음으로 들어감

                }
                (activity as BasicActivity).setView(5)

            }
            builder.setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->

            }
            builder.show()

        }

        return rootView
    }

}

