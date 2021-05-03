package com.project.churchschool.Activity

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_password_recreate.*

class PasswordRecreateActivity : BasicActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recreate)

        // Initialize Firebase Auth
        auth = Firebase.auth

        PasswordRecreateBttn.setOnClickListener {
            recreatePassword()
        }

        gotoLoginBttn.setOnClickListener {
            gotoLogInActivity(this)
            finish()
        }
    }

    private fun recreatePassword(){
        val emailAddress = EmailAddressEditText_RecreatePassword.text.toString()

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "입력하신 주소로 이메일이 발송되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }
}