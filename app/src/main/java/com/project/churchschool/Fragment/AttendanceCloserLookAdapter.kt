package com.project.churchschool.Fragment

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.QR_AttendanceData
import com.project.churchschool.R

class AttendanceCloserLookAdapter(val NameList: MutableSet<String>?,
                                  val activity: Activity?,
                                  val fragment: Fragment,
                                  val selectedAttndndceData: QR_AttendanceData?,
                                  val isModifyMode : Boolean) :
    RecyclerView.Adapter<AttendanceCloserLookAdapter.StudentsViewHolder>() {

    class StudentsViewHolder(val StudentList: CardView) : RecyclerView.ViewHolder(StudentList)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AttendanceCloserLookAdapter.StudentsViewHolder {

        val StList = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_list, parent, false) as CardView
        val studentsViewHolder = StudentsViewHolder(StList)


        return studentsViewHolder
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        var StName = holder.StudentList.findViewById<TextView>(R.id.StName)
        var O_Bttn = holder.StudentList.findViewById<RadioButton>(R.id.O_bg)
        var X_Bttn = holder.StudentList.findViewById<RadioButton>(R.id.X_bg)
        Log.d(ContentValues.TAG, selectedAttndndceData.toString())
        var tmp : String? =  NameList?.elementAt(position).toString()
        Log.d(ContentValues.TAG, tmp.toString())
        StName.text = tmp


//        if(isModifyMode){
//            O_Bttn.isEnabled = true
//            X_Bttn.isEnabled = true
//        }else{
//            O_Bttn.isEnabled = false
//            X_Bttn.isEnabled = false
//        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = selectedAttndndceData!!.attndnceData?.size ?: 0
}