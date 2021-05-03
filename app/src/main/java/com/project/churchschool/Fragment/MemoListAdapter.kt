package com.project.churchschool.Fragment

import android.app.Activity
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.MemoData
import com.project.churchschool.DataClass.SQLiteHelper
import com.project.churchschool.R

class MemoListAdapter(private val MemoList: ArrayList<MemoData>?, val activity: Activity?, val fragment: Fragment) :
    RecyclerView.Adapter<MemoListAdapter.MemoViewHolder>() {



    class MemoViewHolder(val MemosList: CardView) : RecyclerView.ViewHolder(MemosList)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MemoListAdapter.MemoViewHolder {



        val MemosList = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memo_list, parent, false) as CardView
        val memoViewHolder = MemoViewHolder(MemosList)



        return memoViewHolder
    }


    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        var date = holder.MemosList.findViewById<TextView>(R.id.MemoList_date)
        var title = holder.MemosList.findViewById<TextView>(R.id.MemoList_Title)

        date.text = MemoList?.get(position)?.date
        title.text = MemoList?.get(position)?.title.toString()

        holder.MemosList.setOnClickListener {
            (activity as BasicActivity).setSelectedMemoData(MemoList?.get(position))
            (activity as BasicActivity).setView(7)

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = MemoList?.size ?: 0
}