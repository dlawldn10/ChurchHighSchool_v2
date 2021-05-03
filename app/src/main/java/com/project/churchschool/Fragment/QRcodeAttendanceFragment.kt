package com.project.churchschool.Fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.model.Document
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.FinalAttndnceData
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.DataClass.QR_AttendanceData
import com.project.churchschool.R
import kotlinx.android.synthetic.main.fragment_attendance_qr.view.*
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.*
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.Myclass_Attndnce_profileImage
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.Tchr_Attndnce_left_arrow
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.Tchr_Attndnce_right_arrow


class QRcodeAttendanceFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(com.project.churchschool.R.layout.fragment_attendance_qr,
            container,
            false)
        val db = FirebaseFirestore.getInstance()
        var auth: FirebaseAuth = Firebase.auth
        val user = auth.currentUser
        var currentUserInfo : MemberInfo
        val currentUser = db.collection("users").document(user?.email.toString())
        val profilePhoto = rootView.Myclass_Attndnce_profileImage
        var attndnceInfo : ArrayList<MutableMap<String, Any>> = arrayListOf()
        var FinalattndnceInfo : ArrayList<QR_AttendanceData> = arrayListOf()
        val thisYear = (activity as BasicActivity).getYear()
        var thisMonth_int: Int? = (activity as BasicActivity).getMonth().toInt()
        var thisMonth_string : String = (activity as BasicActivity).getMonth()
        var today : String = (activity as BasicActivity).getDate()


        val thisMonthDataRef = db.collection("attendances").document(thisYear).collection(thisMonth_string)

        setUI(rootView, thisMonth_int)

        currentUser.get().addOnSuccessListener { documentSnapshot ->
            currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!

            rootView.findViewById<TextView>(R.id.Teaher_MyName).text = currentUserInfo.name + " " + currentUserInfo.group
            rootView.findViewById<TextView>(R.id.Year_TextView).text = thisYear + "년"
            rootView.findViewById<TextView>(R.id.Month_TextView).text = thisMonth_string + "월"

            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500)
                .into(profilePhoto)

            db.collection("attendances").document(thisYear).collection(thisMonth_string)
                .get().addOnSuccessListener { result ->

                    var dateList : ArrayList<String> = arrayListOf()
                    for (document in result) {
                        attndnceInfo.add(document.data)
                        Log.d(TAG, "${document.id} => ${document.data}")
                        dateList.add(document.id)
                        Log.d(TAG, "$dateList")
                    }
                    for(i in 0..dateList.size-1){
                        FinalattndnceInfo.add(QR_AttendanceData(attndnceInfo[i], dateList[i]))
                    }
                    viewManager = LinearLayoutManager(context)
                    viewAdapter = QRAttndnceAdapter(FinalattndnceInfo, activity, this)


                    recyclerView =
                        rootView.findViewById<RecyclerView>(R.id.Teaher_myclass_Attndnce)
                            .apply {
                                setHasFixedSize(true)
                                layoutManager = viewManager
                                adapter = viewAdapter
                                //뷰메니저와 어댑터 적용하기.
                            }

                }
        }

        rootView.Tchr_Attndnce_right_arrow.setOnClickListener {     //다음월 데이터 출력하기
            attndnceInfo.clear()
            FinalattndnceInfo.clear()
            thisMonth_int = thisMonth_int?.plus(1)
            if (thisMonth_int in 1..9) {
                thisMonth_string = "0" + thisMonth_int.toString()
            }else{
                thisMonth_string = thisMonth_int.toString()
            }
            setUI(rootView, thisMonth_int)


            currentUser.get(Source.CACHE).addOnSuccessListener { documentSnapshot ->
                currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
                rootView.findViewById<TextView>(R.id.Teaher_MyName).text = currentUserInfo.name + " " + currentUserInfo.group
                rootView.findViewById<TextView>(R.id.Year_TextView).text = thisYear + "년"
                rootView.findViewById<TextView>(R.id.Month_TextView).text = thisMonth_string + "월"
                Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500)
                    .into(profilePhoto)


                db.collection("attendances").document(thisYear).collection(thisMonth_string)
                    .get().addOnSuccessListener { result ->
                        var dateList : ArrayList<String> = arrayListOf()
                        for (document in result) {
                            attndnceInfo.add(document.data)
                            Log.d(TAG, "${document.id} => ${document.data}")
                            dateList.add(document.id)
                            Log.d(TAG, "$dateList")
                        }
                        for(i in 0..dateList.size-1){
                            FinalattndnceInfo.add(QR_AttendanceData(attndnceInfo[i], dateList[i]))
                        }
                        viewManager = LinearLayoutManager(requireContext())
                        viewAdapter = QRAttndnceAdapter(FinalattndnceInfo, activity, this)


                        recyclerView =
                            rootView.findViewById<RecyclerView>(R.id.Teaher_myclass_Attndnce)
                                .apply {
                                    setHasFixedSize(true)
                                    layoutManager = viewManager
                                    adapter = viewAdapter
                                    //뷰메니저와 어댑터 적용하기.
                                }

                    }
            }
        }

        rootView.Tchr_Attndnce_left_arrow.setOnClickListener {
            attndnceInfo.clear()
            FinalattndnceInfo.clear()
            thisMonth_int = thisMonth_int?.minus(1)
            if (thisMonth_int in 1..9) {
                thisMonth_string = "0" + thisMonth_int.toString()
            }else{
                thisMonth_string = thisMonth_int.toString()
            }
            setUI(rootView, thisMonth_int)

            currentUser.get(Source.CACHE).addOnSuccessListener { documentSnapshot ->
                currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
                rootView.findViewById<TextView>(R.id.Teaher_MyName).text = currentUserInfo.name + " " + currentUserInfo.group
                rootView.findViewById<TextView>(R.id.Year_TextView).text = thisYear + "년"
                rootView.findViewById<TextView>(R.id.Month_TextView).text = thisMonth_string + "월"
                Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500)
                    .into(profilePhoto)

                db.collection("attendances").document(thisYear).collection(thisMonth_string)
                    .get().addOnSuccessListener { result ->
                        var dateList : ArrayList<String> = arrayListOf()
                        for (document in result) {
                            attndnceInfo.add(document.data)
                            Log.d(TAG, "${document.id} => ${document.data}")
                            dateList.add(document.id)
                            Log.d(TAG, "$dateList")
                        }
                        for(i in 0..dateList.size-1){
                            FinalattndnceInfo.add(QR_AttendanceData(attndnceInfo[i], dateList[i]))
                        }
                        viewManager = LinearLayoutManager(requireContext())
                        viewAdapter = QRAttndnceAdapter(FinalattndnceInfo, activity, this)


                        recyclerView =
                            rootView.findViewById<RecyclerView>(R.id.Teaher_myclass_Attndnce)
                                .apply {
                                    setHasFixedSize(true)
                                    layoutManager = viewManager
                                    adapter = viewAdapter
                                    //뷰메니저와 어댑터 적용하기.
                                }

                    }
            }
        }

        rootView.gotoCheckAttndnce_Bttn_qr.setOnClickListener {
            (activity as BasicActivity).setView(6)

        }


        return rootView

    }

    fun setUI(rootView: View, thisMonth_int: Int?){
        if (thisMonth_int == 1) {
            rootView.Tchr_Attndnce_left_arrow.visibility = View.INVISIBLE
            rootView.Tchr_Attndnce_right_arrow.visibility = View.VISIBLE
        } else if (thisMonth_int in 2..11) {
            rootView.Tchr_Attndnce_left_arrow.visibility = View.VISIBLE
            rootView.Tchr_Attndnce_right_arrow.visibility = View.VISIBLE
        } else if (thisMonth_int == 12) {
            rootView.Tchr_Attndnce_right_arrow.visibility = View.INVISIBLE
            rootView.Tchr_Attndnce_left_arrow.visibility = View.VISIBLE
        }
    }

}