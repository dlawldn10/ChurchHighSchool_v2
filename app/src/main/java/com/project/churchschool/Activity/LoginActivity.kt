package com.project.churchschool.Activity

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BasicActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        updateUI(currentUser)

        GoSignUpPage.setOnClickListener {
            finish()
            gotoSignUpActivity(this)

        }
        LogInButton.setOnClickListener {
            logIn()
        }

        PasswordReCreateBttn.setOnClickListener {
            finish()
            gotoPasswordRecreateActivity(this)
        }
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){    //로그인 된 상태이면
            gotoMainActivity(this)      //메인 액티비티로 이동
            finish()
        }
    }

    private fun logIn(){
        var email : String = EmailEditText_LogIn.text.toString()
        var password : String = PasswordEditText_Login.text.toString()

        if(email.isEmpty() || password.isEmpty() || !GroupCheck_Login()) {
            Toast.makeText(
                baseContext, "정보를 모두 입력하여 주십시오.",
                Toast.LENGTH_SHORT
            ).show()
        }else{

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {    //성공했을 때
                        Toast.makeText(
                            baseContext, "로그인되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        gotoMainActivity(this)
                        finish()    //로그인 화면 끝내기

                    } else {    //실패했을 때
                        Toast.makeText(
                            baseContext, "이메일 또는 비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
        }


    }



}