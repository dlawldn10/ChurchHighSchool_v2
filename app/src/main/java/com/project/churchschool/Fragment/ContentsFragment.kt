package com.project.churchschool.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.Activity.GalleryActivity
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.fragment_contents.*
import kotlinx.android.synthetic.main.fragment_contents.view.*
import kotlinx.android.synthetic.main.fragment_myclass_attendance.view.*
import java.io.File
import java.io.FileInputStream


class ContentsFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var contentUri: String = ""
    private val externalStorageRQ : Int = 1
    private val storage : FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_contents, container, false)

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings     //오프라인일때 캐시 사용하여 로드하기 설정.
        val user = (activity as BasicActivity).getUser()
        val currentUser = db.collection("users").document(user?.email.toString())
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
            if (currentUserInfo?.group == "학생") {
                rootView.content_modify_bttn.visibility = View.GONE
            }
        }

        downloadContent()   //원래 있던 주보 다운로드
        

        rootView.findViewById<Button>(R.id.content_modify_bttn).setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {     //권한이 있을 때
                    //페이지 이동
                    gotoGallery()
                }
                else -> {   //권한이 없을때
                    //권한 물어보기
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        externalStorageRQ
                    )

                }

            }
        }
        return rootView
    }

    fun gotoGallery(){
        val intent = Intent(requireContext(), GalleryActivity::class.java)
        //startActivityForResult는 A에서 이동한 B Activity가 종료되면서 다시 A로 데이터를 보내는 방식.
        intent.putExtra("from", "ContentsFragment")
        startActivityForResult(intent, 200)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    gotoGallery()
                } else {

                    Toast.makeText(
                        requireContext(), "프로필 사진을 변경하려면 권한이 필요합니다.\n'설정>앱>문화교회 고등부>권한' 에서 설정해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                return
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
//                100 -> {
//                    val result = data?.getStringExtra("PROFILE_PHOTO_PATH")
//                    profilePhotoUri = result
//                    Glide.with(this).load(result).override(500).into(ProfilePhoto)
//                }
                200 -> {
                    val result = data?.getStringExtra("CONTENT_PATH")
                    contentUri = result!!
                    Glide.with(this).load(result).override(500).into(contentView_imageView)
                    updateOnDB(contentUri)
                }
            }
        }
    }

    fun updateOnDB(uri : String) {

        val stream = FileInputStream(File(uri))
        val storageRef = storage.reference
        val profileImageRef = storageRef.child("contents/thisWeek.jpg")

        val uploadTask = profileImageRef.putStream(stream)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                    Log.e("에러", it.toString())
                }
            }
            profileImageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    requireContext(), "주보가 업데이트 되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(), "업로드 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }

    fun downloadContent() {
        val storageRef = storage.reference
        val contentImageRef = storageRef.child("contents/thisWeek.jpg")
        contentImageRef.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).override(500).into(contentView_imageView)

        }.addOnFailureListener {
            Toast.makeText(
                requireContext(), "주보 업데이트가 필요합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}