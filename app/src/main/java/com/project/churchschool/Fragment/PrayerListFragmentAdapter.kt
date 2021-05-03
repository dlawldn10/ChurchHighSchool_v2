package com.project.churchschool.Fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.project.churchschool.DataClass.PrayerData
import com.project.churchschool.R

class PrayerListFragmentAdapter(private val PrayerList: ArrayList<PrayerData>?) :
    RecyclerView.Adapter<PrayerListFragmentAdapter.PrayersViewHolder>() {

    class PrayersViewHolder(val PrayList: CardView) : RecyclerView.ViewHolder(PrayList)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PrayerListFragmentAdapter.PrayersViewHolder {

        val dateList = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prayers, parent, false) as CardView
        val datesViewHolder = PrayersViewHolder(dateList)


        return datesViewHolder
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PrayersViewHolder, position: Int) {
        var preView = holder.PrayList.findViewById<TextView>(R.id.Prayer_preview_textView)
        var date = holder.PrayList.findViewById<TextView>(R.id.Prayerist_date)

        preView.text = PrayerList?.get(position)?.text
        date.text = PrayerList?.get(position)?.date

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = PrayerList?.size ?: 0

    fun addChat(prayerData: PrayerData?){
        PrayerList?.add(prayerData!!)
        notifyItemInserted(getItemCount()-1)    //데이터 갱신.
        Log.e("결과 addChat", PrayerList.toString())
    }
}