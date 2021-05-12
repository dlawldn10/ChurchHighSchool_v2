package com.project.churchschool.Activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.project.churchschool.R

//프로그래스바 커스텀해서 쓰는 방법
class ProgressDialog(context: Context?) : Dialog(context!!){

    init {
        setCanceledOnTouchOutside(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.progress_dialog)
    }
}