package com.project.churchschool.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.Fragment.HomeFragment
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BasicActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth

        SignUpButton.setOnClickListener{    //회원가입 버튼을 눌렀을 때
            signUp()
        }


    }

    private fun CheckGroup() : String{      //체크한 항목에 따라 정보 구분하기
        var group:String = ""

        if(StudentRadioBttn_Signup.isChecked) {
            group = "학생"
        }else if(TeacherRadioBttn_Signup.isChecked){
            group = "교사"
        }

        return group
    }

    private fun signUp(){

        var email : String = EmailEditText.text.toString()
        var password : String = PasswordEditText.text.toString()
        var passwordCheck : String = PasswordCheckEditText.text.toString()

        if(email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty() || !GroupCheck_SignUp()) {     //하나라도 비어있을 경우
            Toast.makeText(
                baseContext, "정보를 모두 입력하여 주십시오.",
                Toast.LENGTH_SHORT
            ).show()
        }else{  //모두 입력했는데,

            if (!password.equals(passwordCheck)) {  //비밀번호 일치하지 않는 경우
                Toast.makeText(
                    baseContext, "비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {    //비밀번호도 일치하면..ok!

                val memberInfo = MemberInfo(
                    "이름",
                    CheckGroup(),
                    email,
                    password,
                    "전화번호",
                    "생년월일",
                    "주소",
                    "프로필 사진"
                )
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        val user = auth.currentUser

                        if (task.isSuccessful) {    //회원등록 성공했을 때
                            Toast.makeText(
                                baseContext, "회원가입이 정상적으로 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("결과", "auth등록 성공")
                            setMemberInfo(memberInfo, "SignUpActivity")   //dp에 회원정보 등록
                            gotoMainActivity(this)
                            finish()

                        } else {    //실패했을 때
                            val errorType1: String =
                                "The email address is already in use by another account."
                            val errorType2: String =
                                "The given password is invalid. [ Password should be at least 6 characters ]"

                            val error: Exception? = task.exception
                            if (error?.message.toString() == errorType1) {
                                Toast.makeText(
                                    baseContext, "동일한 이메일이 이미 존재합니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (error?.message.toString() == errorType2) {
                                Toast.makeText(
                                    baseContext, "비밀번호는 최소 6자리 이상이어야 합니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }


                    }
            }
        }

    }
}