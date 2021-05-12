package com.project.churchschool.Fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.FinalAttndnceData
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.DataClass.QR_AttendanceData
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.fragment_attndnce_chck.view.*
import kotlin.collections.ArrayList


class AttendanceCloserLookFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val db = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    val currentUser = db.collection("users").document(user?.email.toString())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_attndnce_chck, container, false)

//        (activity as BasicActivity).setSelectedAttendnceData(null)
        val Save_Bttn = rootView.findViewById<ImageView>(R.id.Attndnce_save_imageView)
        val Update_Bttn = rootView.findViewById<ImageView>(R.id.Attndnce_update_imageView)
        val Cancel_Bttn = rootView.findViewById<ImageView>(R.id.Attendnce_cancel_imageView)
        val Screen = rootView.findViewById<ImageView>(R.id.Screen)
        val profilePhoto = rootView.AttndnceChk_profileImage
        val selectedData : QR_AttendanceData? = (activity as BasicActivity).getSelectedAttendnceDataList()

        currentUser.get(Source.CACHE).addOnSuccessListener { documentSnapshot ->
            val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500).into(profilePhoto)
        }

        if(selectedData != null) {  //클릭해서 들어온 경우UI
            Save_Bttn.visibility = View.GONE
            Update_Bttn.visibility = View.GONE
            Cancel_Bttn.visibility = View.GONE
            }
        else{
            //기본UI
            Update_Bttn.visibility = View.GONE
            Save_Bttn.visibility = View.VISIBLE
            Cancel_Bttn.visibility = View.VISIBLE
        }

        //데이터 로드
        loadData(rootView, selectedData, false)


        return rootView
    }

    private fun makeAlert_delete(what : String, currentUserInfo: MemberInfo, selectedData: AttndnceData?){   //왜 화면전환 안되는지 확인하기
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("$what 삭제")
        builder.setMessage("이 $what" + "를 삭제 하시겠습니까?")
        builder.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
            deleteData(currentUserInfo, selectedData)

        }
        builder.setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()

    }

    fun makeAlert_calncel(what : String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("$what 취소")
        builder.setMessage("이 $what" + "작성을 취소 하시겠습니까?")
        builder.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
            (activity as BasicActivity).setView(2)
        }
        builder.setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()
    }

    fun deleteData(currentUserInfo : MemberInfo, selectedData : AttndnceData?){
        db.collection("attendances")
            .document(currentUserInfo.name.toString())
            .update("attndnceDataList", FieldValue.arrayRemove(selectedData))
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(), "출석정보가 삭제 되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                (activity as BasicActivity).setView(2)
            }
    }



    fun loadData(rootView : View, selectedData: QR_AttendanceData?, isModifyMode : Boolean){
        var studentList = mutableSetOf<String>()
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
            if(selectedData != null) {
                rootView.findViewById<TextView>(R.id.attendanceChckDate).text = selectedData.date
                studentList = selectedData.attndnceData.keys
            }else{
                rootView.findViewById<TextView>(R.id.attendanceChckDate).text = (activity as BasicActivity).getYYYYMMDD()

            }
            rootView.findViewById<TextView>(R.id.Teaher_MyName).text = currentUserInfo.name + " " + currentUserInfo.group

//            for(i in 0..selectedData!!.attndnceData.size-1){
//                studentList = selectedData.attndnceData[i]
//            }

//            Log.d(ContentValues.TAG, "$selectedData")
            viewManager = LinearLayoutManager(requireContext())
            if(selectedData != null){
                viewAdapter = AttendanceCloserLookAdapter(studentList, activity, this, selectedData, isModifyMode)
            }else{
                viewAdapter = AttendanceCloserLookAdapter(studentList, activity, this, null, isModifyMode)
            }

            recyclerView =
                rootView.findViewById<RecyclerView>(R.id.Teaher_check_Attndnce_recycler_view).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                    //뷰메니저와 어댑터 적용하기.
                }

        }
    }


}