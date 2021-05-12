package com.project.churchschool.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.PrayerData
import com.project.churchschool.R


class PrayerListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val database = Firebase.database
    var myRef = database.getReference("prayers").orderByKey()

    var oldestPrayerKey: String? = ""
    var contents = arrayListOf<PrayerData?>()
    var contents_get = arrayListOf<PrayerData?>()
    var oldPrayer_get = arrayListOf<String?>()

    var contentsLimit = 15

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_prayerlist, container, false)

        rootView.findViewById<ExtendedFloatingActionButton>(R.id.Write_Prayer_Bttn)
            .setOnClickListener {
                (activity as BasicActivity).setView(8)
            }

        myRef.limitToLast(contentsLimit).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    contents.add(0, item.getValue<PrayerData>())
                    contents_get.add(0, item.getValue<PrayerData>())
                    oldPrayer_get.add(item.key)
                }
                oldestPrayerKey = oldPrayer_get.get(0)

                viewManager = LinearLayoutManager(context)
                viewAdapter = PrayerListFragmentAdapter(contents)
                recyclerView = rootView.findViewById<RecyclerView>(R.id.prayer_list_recycler_view).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            //스크롤 하단 체크
                            if (!canScrollVertically(1)) {

                                myRef.endAt(oldestPrayerKey).limitToLast(contentsLimit)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            contents_get.clear() //임시저장 위치
                                            oldPrayer_get.clear()
                                            for (item in dataSnapshot.children) {
                                                contents_get.add(0,
                                                    item.getValue<PrayerData>())
                                                oldPrayer_get.add(item.key)
                                            }
                                            //불러오는 중인지, 전부 불러왔는지 if문
                                            if (contents_get.size > 1) { //1개라도 있으면 불러옴
                                                //contents 뒤에 추가
                                                contents.addAll(contents_get)
                                                oldestPrayerKey = oldPrayer_get.get(0)
                                                //메시지 갱신 위치
                                                adapter!!.notifyItemInserted(0)
                                            } else {
                                                Toast.makeText(
                                                    context, "더이상 새로운 기도제목이 없습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            Toast.makeText(context,
                                                "예상치 못한 오류가 발생했습니다. 다시 실행해주세요.",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }

                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                        }
                    })

                    }///여기까지 리사이클러뷰
                }///여기까지 onDataChange

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context,
                        "예상치 못한 오류가 발생했습니다. 다시 실행해주세요.",
                        Toast.LENGTH_SHORT).show()
                }
            })


            return rootView


    }


}


