package com.project.churchschool.Fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.PrayerData
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_contents.*


class PrayerListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val database = Firebase.database
    var myRef = database.getReference("prayers")
    private var msgList: ArrayList<PrayerData?>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_prayerlist, container, false)

        msgList = ArrayList()
        viewManager = LinearLayoutManager(context)
        (viewManager as LinearLayoutManager).reverseLayout = true
        (viewManager as LinearLayoutManager).stackFromEnd = true
        viewAdapter = PrayerListFragmentAdapter(msgList)

        rootView.findViewById<ExtendedFloatingActionButton>(R.id.Write_Prayer_Bttn)
            .setOnClickListener {
                (activity as BasicActivity).setView(8)
            }
        //////
        var contents_get = arrayListOf<PrayerData?>()
        var oldPost_get = arrayListOf<String?>()
        var oldestPostId: String? = ""
        myRef.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var prayer = snapshot.getValue<PrayerData>()
                (viewAdapter as PrayerListFragmentAdapter).addChat(prayer)
//                msgList!!.add(0, snapshot.getValue<PrayerData>())
                contents_get.add(0, snapshot.getValue<PrayerData>())
                oldPost_get.add(snapshot.key.toString())
//                for (item in snapshot.children) {
//
//                }
                oldestPostId = oldPost_get[0]
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context,
                    "예상치 못한 오류가 발생했습니다. 다시 실행해주세요.",
                    Toast.LENGTH_SHORT).show()
            }
        })


        recyclerView = rootView.findViewById<RecyclerView>(R.id.prayer_list_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1)) {
//                        myRef.addChildEventListener(object : ChildEventListener {
//
//                            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
////                                msgList!!.clear()
//                                contents_get.clear()
//                                oldPost_get.clear()
//                                var prayer = snapshot.getValue<PrayerData>()
//                                (adapter as PrayerListFragmentAdapter).addChat(prayer)
//                                contents_get.add(0, snapshot.getValue<PrayerData>())
//                                oldPost_get.add(0, snapshot.key.toString())
//                                oldestPostId = oldPost_get[0]
//                            }
////
//                            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//
//                            }
//
//                            override fun onChildRemoved(snapshot: DataSnapshot) {
//                                TODO("Not yet implemented")
//                            }
//
//                            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                                TODO("Not yet implemented")
//                            }
//
//                            override fun onCancelled(databaseError: DatabaseError) {
//                                Toast.makeText(context,
//                                    "예상치 못한 오류가 발생했습니다. 다시 실행해주세요.",
//                                    Toast.LENGTH_SHORT).show()
//                            }
//                        })

                    }
                }
            })




            return rootView
        }
    }
}


