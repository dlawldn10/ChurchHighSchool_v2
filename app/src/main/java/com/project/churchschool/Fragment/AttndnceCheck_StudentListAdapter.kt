package com.project.churchschool.Fragment

import android.app.Activity
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
import com.project.churchschool.R

class AttndnceCheck_StudentListAdapter(private val myStudentList: ArrayList<String>?,
                                       val activity: Activity?,
                                       val fragment: Fragment,
                                       val selectedAttndndceData: AttndnceData?,
val isModifyMode : Boolean) :
    RecyclerView.Adapter<AttndnceCheck_StudentListAdapter.StudentsViewHolder>() {

    class StudentsViewHolder(val StudentList: CardView) : RecyclerView.ViewHolder(StudentList)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AttndnceCheck_StudentListAdapter.StudentsViewHolder {

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


        var StudentName : String = myStudentList!![position]
        StName.text = StudentName

        if(selectedAttndndceData != null){
            if(selectedAttndndceData.attndnceData?.get(StudentName) == true){
                O_Bttn.isChecked = true
                O_Bttn.buttonDrawable = getDrawable(holder.StudentList.context, R.drawable.blue_backgound)
                (activity as BasicActivity).saveAttndnceData(StudentName, true)
            }else{
                X_Bttn.isChecked = true
                X_Bttn.buttonDrawable = getDrawable(holder.StudentList.context, R.drawable.blue_backgound)
                (activity as BasicActivity).saveAttndnceData(StudentName, false)
            }

            if(isModifyMode){
                O_Bttn.isEnabled = true
                X_Bttn.isEnabled = true
            }else{
                O_Bttn.isEnabled = false
                X_Bttn.isEnabled = false
            }

        }




        O_Bttn.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                button.buttonDrawable = getDrawable(holder.StudentList.context, R.drawable.blue_backgound)
                (activity as BasicActivity).saveAttndnceData(StudentName, true)

            }else{
                button.buttonDrawable = getDrawable(holder.StudentList.context, R.drawable.gray_background)
            }
        }

        X_Bttn.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                button.buttonDrawable = getDrawable(holder.StudentList.context, R.drawable.blue_backgound)
                (activity as BasicActivity).saveAttndnceData(StudentName, false)
            }else{
                button.buttonDrawable = getDrawable(holder.StudentList.context, R.drawable.gray_background)
            }

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myStudentList?.size ?: 0
}