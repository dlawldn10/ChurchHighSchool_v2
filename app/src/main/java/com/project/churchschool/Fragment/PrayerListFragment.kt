package com.project.churchschool.Fragment

import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.PrayerData
import com.project.churchschool.R
import kotlinx.android.synthetic.main.fragment_contents.*


class PrayerListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val database = Firebase.database
    val myRef = database.getReference("prayers")
    private var msgList : ArrayList<PrayerData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_prayerlist, container, false)

        msgList = ArrayList()
        viewManager = LinearLayoutManager(requireContext())
        (viewManager as LinearLayoutManager).reverseLayout = true
        (viewManager as LinearLayoutManager).stackFromEnd = true
        viewAdapter = PrayerListFragmentAdapter(msgList)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.prayer_list_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        rootView.findViewById<ExtendedFloatingActionButton>(R.id.Write_Prayer_Bttn).setOnClickListener {
            (activity as BasicActivity).setView(8)
        }

        val childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                var prayer = dataSnapshot.getValue<PrayerData>()
                (viewAdapter as PrayerListFragmentAdapter).addChat(prayer)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
//                val newComment = dataSnapshot.getValue<Comment>()
//                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
//                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
//                val movedComment = dataSnapshot.getValue<Comment>()
//                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        myRef.addChildEventListener(childEventListener)



        return rootView
    }


}