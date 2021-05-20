package com.project.churchschool.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
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
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.DataClass.QR_AttendanceData
import com.project.churchschool.R

class StudentListAdapter(val NameList: MutableSet<String>,
                         val activity: Activity?,
                         val fragment: Fragment) :
    RecyclerView.Adapter<StudentListAdapter.StudentsViewHolder>() {

    class StudentsViewHolder(val StudentList: CardView) : RecyclerView.ViewHolder(StudentList)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): StudentListAdapter.StudentsViewHolder {

        val StList = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_student_list, parent, false) as CardView
        val studentsViewHolder = StudentsViewHolder(StList)


        return studentsViewHolder
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        var StName = holder.StudentList.findViewById<TextView>(R.id.StName)
        var tmp : String? =  NameList?.elementAt(position).toString() + " 학생"
        StName.text = tmp

        holder.StudentList.setOnClickListener {
            makeAlert(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = NameList?.size ?: 0

    private fun makeAlert (position: Int){   //왜 화면전환 안되는지 확인하기
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("출석 체크")
        builder.setMessage("이 학생의 출석을 체크하시겠습니까?")
        builder.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
            (activity as BasicActivity).setSelectedStudentName(NameList.elementAt(position))    //추가하고싶은 학생 이름 저장
            (activity as BasicActivity).setView(2)
        }
        builder.setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()

    }
}