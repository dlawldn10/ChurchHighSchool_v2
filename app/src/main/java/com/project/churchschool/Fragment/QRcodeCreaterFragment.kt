package com.project.churchschool.Fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.project.churchschool.DataClass.MemberInfo
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.*


class QRcodeCreaterFragment : Fragment() {
    private var iv: ImageView? = null
    private var text: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(com.project.churchschool.R.layout.fragment_create_qr, container, false)

        val db = FirebaseFirestore.getInstance()
        var auth: FirebaseAuth = Firebase.auth
        val user = auth.currentUser
        var currentUserInfo : MemberInfo
        val currentUser = db.collection("users").document(user?.email.toString())
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            if(getActivity() != null && isAdded()) {
                currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!

                iv =
                    rootView.findViewById<ImageView>(com.project.churchschool.R.id.qrcode_imageView)
                text = currentUserInfo.email
                val multiFormatWriter = MultiFormatWriter()
                try {
                    val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
                    val barcodeEncoder = BarcodeEncoder()
                    val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                    iv!!.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }

        }

        return rootView
    }

}