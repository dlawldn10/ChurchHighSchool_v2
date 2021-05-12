package com.project.churchschool.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.R
import kotlinx.android.synthetic.main.fragment_attendance_qr.*
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.*


class QRcodeScannerActivity : BasicActivity() {
    val db = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth

    var thisMonth_string : String = getMonth()

    private var qrScan: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_scan_qr);

        startScan()


    }

    fun startScan(){
        qrScan = IntentIntegrator(this);
        qrScan!!.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan!!.setPrompt("QR코드를 비춰 주세요");
        qrScan!!.initiateScan();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                finish()
                // todo
            } else {
                startScan()
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                var presentUser = db.collection("users").document(result.contents).get()
                presentUser.addOnSuccessListener{ documentSnapshot ->
                    val presentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
                    val data = hashMapOf(presentUserInfo.name + " " + presentUserInfo.group to true)
                    db.collection("attendances").document(getYear()).collection(getMonth()).document(getYYYYMMDD())
                        .set(data, SetOptions.merge())
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}