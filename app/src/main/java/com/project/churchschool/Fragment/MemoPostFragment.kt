package com.project.churchschool.Fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.DataClass.MemoData
import com.project.churchschool.DataClass.SQLiteHelper
import com.project.churchschool.R
import kotlinx.android.synthetic.main.fragment_memo_post.*

class MemoPostFragment : Fragment() {

    lateinit var dbHelper : SQLiteHelper
    lateinit var database : SQLiteDatabase
    var this_primkey : String? = ""

    var text : String = ""
    var title : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_memo_post, container, false)

        val deleteBttn = rootView.findViewById<ImageView>(R.id.Memo_delete_Bttn) //삭제
        val saveBttn = rootView.findViewById<ImageView>(R.id.Memo_Save_Bttn)    //완료, 저장
        val updateBttn = rootView.findViewById<ImageView>(R.id.Memo_Update_Bttn)
        val modifyBttn = rootView.findViewById<ImageView>(R.id.Memo_modify_Bttn)    //수정
        val cancelBttn = rootView.findViewById<ImageView>(R.id.Memo_Cancel_Bttn)

        val titleEditText = rootView.findViewById<EditText>(R.id.Memo_Title_EditText)
        val insideEditText = rootView.findViewById<EditText>(R.id.Memo_Inside_EditText)
        val dbName = (activity as BasicActivity).get_myDBname()

        val YYYYMMDD = (activity as BasicActivity).getYYYYMMDD()

        dbHelper = SQLiteHelper(context, dbName, null, 1)
        database = dbHelper.writableDatabase

        val selectedMemo = (activity as BasicActivity).getSelectedMemoData()



        if(selectedMemo != null){    //메모를 눌러서 들어온 경우.
            rootView.findViewById<EditText>(R.id.Memo_Inside_EditText).setText(selectedMemo.text)
            rootView.findViewById<EditText>(R.id.Memo_Title_EditText).setText(selectedMemo.title)
            this_primkey = selectedMemo.primKey
            saveBttn.visibility = View.GONE
            updateBttn.visibility = View.GONE
            cancelBttn.visibility = View.GONE
            titleEditText.isEnabled = false
            insideEditText.isEnabled = false

            modifyBttn.visibility = View.VISIBLE
            deleteBttn.visibility = View.VISIBLE

        }


        modifyBttn.setOnClickListener { //수정하기 버튼 누르면

            titleEditText.isEnabled = true
            insideEditText.isEnabled = true

            updateBttn.visibility = View.VISIBLE
            saveBttn.visibility = View.GONE

            modifyBttn.visibility = View.GONE

            deleteBttn.visibility = View.VISIBLE
            updateBttn.visibility = View.VISIBLE
            cancelBttn.visibility = View.VISIBLE


        }

        cancelBttn.setOnClickListener {
            makeAlert("메모", "작성 취소")
        }

        updateBttn.setOnClickListener {

            text = insideEditText.text.toString()
            title = titleEditText.text.toString()


            val values = ContentValues()
            values.put("title", title)
            values.put("date", YYYYMMDD)
            values.put("text", text)

            database.update("myMemo", values, "_id =?", arrayOf(this_primkey))
            Toast.makeText(requireContext(), "업데이트 되었습니다.", Toast.LENGTH_SHORT).show()

            saveBttn.visibility = View.GONE
            updateBttn.visibility = View.GONE
            cancelBttn.visibility = View.GONE
            titleEditText.isEnabled = false
            insideEditText.isEnabled = false

            modifyBttn.visibility = View.VISIBLE
            deleteBttn.visibility = View.VISIBLE
        }

        saveBttn.setOnClickListener {

            text = insideEditText.text.toString()
            title = titleEditText.text.toString()

            val values = ContentValues()
            values.put("title", title)
            values.put("date", YYYYMMDD)
            values.put("text", text)

            database.insert("myMemo", null ,values)
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()

            //보기 화면으로 전환
            saveBttn.visibility = View.GONE
            updateBttn.visibility = View.GONE
            cancelBttn.visibility = View.GONE
            titleEditText.isEnabled = false
            insideEditText.isEnabled = false

            modifyBttn.visibility = View.VISIBLE
            deleteBttn.visibility = View.VISIBLE

        }

        deleteBttn.setOnClickListener {
            makeAlert("메모", "삭제")

        }

        return rootView
    }

    private fun makeAlert(what : String, how : String){   //왜 화면전환 안되는지 확인하기
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(what + how)
        builder.setMessage("이 $what" + "를 $how" + "하시겠습니까?")
        builder.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
            if(how == "삭제"){
                database.delete("myMemo", "_id =?", arrayOf(this_primkey))
                Toast.makeText(requireContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                (activity as BasicActivity).setView(4)
            }
            else if(how == "작성 취소"){
                (activity as BasicActivity).setView(4)
            }

        }
        builder.setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()

    }




}

