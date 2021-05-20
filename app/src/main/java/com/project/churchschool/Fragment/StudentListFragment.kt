package com.project.churchschool.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.R
import kotlinx.android.synthetic.main.fragment_all_student_list.*
import kotlinx.android.synthetic.main.fragment_all_student_list.view.*

class StudentListFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val db = FirebaseFirestore.getInstance()
    val allStudentsRef = db.collection("users")




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_all_student_list, container, false)
        var studentList = mutableSetOf<String>()
        val searchView = rootView.Student_search_view

        searchView.isSubmitButtonEnabled = true //검색버튼 나오게 하는 코드

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                // 검색 버튼 누를 때 호출

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출

                return true
            }
        })

        allStudentsRef.whereEqualTo("group", "학생").get().addOnSuccessListener { documents ->
            for (document in documents) {
                var studentInfo = document.toObject(MemberInfo::class.java)!!
                studentList.add(studentInfo.name.toString())
                Log.d("test", studentList.last())
            }
            Toast.makeText(
                requireContext(), "성공",
                Toast.LENGTH_SHORT
            ).show()

            viewManager = LinearLayoutManager(requireContext())
            viewAdapter = StudentListAdapter(studentList, activity, this)

            recyclerView =
                rootView.findViewById<RecyclerView>(R.id.StudentList_recyclerView).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                    //뷰메니저와 어댑터 적용하기.
                }

        }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(), "학생 목록 가져오기 실패",
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText(
                    requireContext(), "네트워크 연결을 확인하세요",
                    Toast.LENGTH_SHORT
                ).show()
            }




        return rootView
    }


}