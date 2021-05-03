package com.project.churchschool.Fragment

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.DataClass.MemoData
import com.project.churchschool.DataClass.SQLiteHelper
import com.project.churchschool.R
import kotlinx.android.synthetic.main.fragment_memo.view.*
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.*


class MemoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    lateinit var dbHelper : SQLiteHelper
    lateinit var database : SQLiteDatabase
    var dataId : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_memo, container, false)
        var auth: FirebaseAuth = Firebase.auth
        val user = auth.currentUser
        dataId = user?.uid.toString() + ".db"
        (activity as BasicActivity).set_myDBname(dataId)
        (activity as BasicActivity).setSelectedMemoData(null)
        val memoData_List = arrayListOf<MemoData>()
        val thisYear = (activity as BasicActivity).getYear()
        var thisMonth_int: Int? = (activity as BasicActivity).getMonth().toInt()
        var thisMonth_string : String? = (activity as BasicActivity).getMonth()
        
        rootView.findViewById<TextView>(R.id.Year_TextView_memo).text = thisYear + "년"
        rootView.findViewById<TextView>(R.id.Month_TextView_memo).text = thisMonth_string + "월"

        dbHelper = SQLiteHelper(context, dataId, null, 1)
        database = dbHelper.writableDatabase
        setUI(rootView, thisMonth_int)

        val query = "SELECT * FROM myMemo WHERE date LIKE '$thisYear/$thisMonth_string%';"
        try {
            var c = database.rawQuery(query,null)
            if(c !=null) {

                while (c.moveToNext()) {

                    val memo = MemoData()
                    memo.primKey = c.getString(c.getColumnIndex("_id"))
                    memo.title = c.getString(c.getColumnIndex("title"))
                    memo.date = c.getString(c.getColumnIndex("date"))
                    memo.text = c.getString(c.getColumnIndex("text"))
                    memoData_List.add(memo)
                }
            }
        }catch (e : SQLiteException){
            Toast.makeText(requireContext(), "본 기기 사용 유저가 아니거나 불러올 메모가 없습니다.", Toast.LENGTH_SHORT).show()
        }

        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = MemoListAdapter(memoData_List, activity, MemoPostFragment())


        recyclerView =
            rootView.findViewById<RecyclerView>(R.id.memo_list_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
                //뷰메니저와 어댑터 적용하기.
            }

        rootView.findViewById<FloatingActionButton>(R.id.addMemo_Bttn).setOnClickListener {
            (activity as BasicActivity).setView(7)
        }

        rootView.MemoList_left_arrow.setOnClickListener {
            memoData_List.clear()
            thisMonth_int = thisMonth_int?.minus(1)
            if (thisMonth_int in 1..9) {
                thisMonth_string = "0" + thisMonth_int.toString()
            }else{
                thisMonth_string = thisMonth_int.toString()
            }
            setUI(rootView, thisMonth_int)
            rootView.findViewById<TextView>(R.id.Month_TextView_memo).text = thisMonth_string + "월"
            val query = "SELECT * FROM myMemo WHERE date LIKE '$thisYear/$thisMonth_string%';"
            try {
                var c = database.rawQuery(query,null)
                if(c !=null) {

                    while (c.moveToNext()) {

                        val memo = MemoData()
                        memo.primKey = c.getString(c.getColumnIndex("_id"))
                        memo.title = c.getString(c.getColumnIndex("title"))
                        memo.date = c.getString(c.getColumnIndex("date"))
                        memo.text = c.getString(c.getColumnIndex("text"))
                        memoData_List.add(memo)
                    }
                }
            }catch (e : SQLiteException){
                Toast.makeText(requireContext(), "본 기기 사용 유저가 아니거나 불러올 메모가 없습니다.", Toast.LENGTH_SHORT).show()
            }

            viewManager = LinearLayoutManager(requireContext())
            viewAdapter = MemoListAdapter(memoData_List, activity, MemoPostFragment())


            recyclerView =
                rootView.findViewById<RecyclerView>(R.id.memo_list_recycler_view).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                    //뷰메니저와 어댑터 적용하기.
                }
        }

        rootView.MemoList_right_arrow.setOnClickListener {
            memoData_List.clear()
            thisMonth_int = thisMonth_int?.plus(1)
            if (thisMonth_int in 1..9) {
                thisMonth_string = "0" + thisMonth_int.toString()
            }else{
                thisMonth_string = thisMonth_int.toString()
            }
            setUI(rootView, thisMonth_int)
            rootView.findViewById<TextView>(R.id.Month_TextView_memo).text = thisMonth_string + "월"
            val query = "SELECT * FROM myMemo WHERE date LIKE '$thisYear/$thisMonth_string%';"
            try {
                var c = database.rawQuery(query,null)
                if(c !=null) {

                    while (c.moveToNext()) {

                        val memo = MemoData()
                        memo.primKey = c.getString(c.getColumnIndex("_id"))
                        memo.title = c.getString(c.getColumnIndex("title"))
                        memo.date = c.getString(c.getColumnIndex("date"))
                        memo.text = c.getString(c.getColumnIndex("text"))
                        memoData_List.add(memo)
                    }
                }
            }catch (e : SQLiteException){
                Toast.makeText(requireContext(), "본 기기 사용 유저가 아니거나 불러올 메모가 없습니다.", Toast.LENGTH_SHORT).show()
            }

            viewManager = LinearLayoutManager(requireContext())
            viewAdapter = MemoListAdapter(memoData_List, activity, MemoPostFragment())


            recyclerView =
                rootView.findViewById<RecyclerView>(R.id.memo_list_recycler_view).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                    //뷰메니저와 어댑터 적용하기.
                }
        }

        return rootView
    }

    fun setUI(rootView: View, thisMonth_int: Int?){
        if (thisMonth_int == 1) {
            rootView.MemoList_left_arrow.visibility = View.INVISIBLE
            rootView.MemoList_right_arrow.visibility = View.VISIBLE
        } else if (thisMonth_int in 2..11) {
            rootView.MemoList_left_arrow.visibility = View.VISIBLE
            rootView.MemoList_right_arrow.visibility = View.VISIBLE
        } else if (thisMonth_int == 12) {
            rootView.MemoList_right_arrow.visibility = View.INVISIBLE
            rootView.MemoList_left_arrow.visibility = View.VISIBLE
        }
    }



}