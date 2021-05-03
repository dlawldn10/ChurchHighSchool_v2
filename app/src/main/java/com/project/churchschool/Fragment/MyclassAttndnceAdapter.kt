package com.project.churchschool.Fragment

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.R

class MyclassAttndnceAdapter(private val AttndnceList: ArrayList<AttndnceData>, val activity: Activity?, val fragment: Fragment) :
    RecyclerView.Adapter<MyclassAttndnceAdapter.StudentsViewHolder>() {

    class StudentsViewHolder(val DatesList: CardView) : RecyclerView.ViewHolder(DatesList)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyclassAttndnceAdapter.StudentsViewHolder {

        val dateList = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class_attndnce, parent, false) as CardView
        val datesViewHolder = StudentsViewHolder(dateList)

        return datesViewHolder
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        var date = holder.DatesList.findViewById<TextView>(R.id.Tchr_Attndnce_date)
        var present = holder.DatesList.findViewById<TextView>(R.id.tchr_Attndnce_present)
        var absent = holder.DatesList.findViewById<TextView>(R.id.tchr_Attndnce_absent)

        var p : Int = 0
        var a : Int = 0

        holder.DatesList.setOnClickListener {
            (activity as BasicActivity).setSelectedAttendnceData(AttndnceList!![position])
            (activity as BasicActivity).setView(6)
        }

        AttndnceList.get(position).attndnceData?.values?.forEach {
            if(it == true){
                p++
            }else if(it == false){
                a++
            }
        }


        date.text = AttndnceList.get(position).date
        present.text = "출석" + p.toString() + "명"
        absent.text = "/결석" + a.toString() + "명"

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = AttndnceList?.size ?: 0
}