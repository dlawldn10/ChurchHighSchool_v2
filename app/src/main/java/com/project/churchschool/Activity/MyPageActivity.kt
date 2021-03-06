package com.project.churchschool.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : BasicActivity() {

    var PICK_IMAGE_FROM_ALBUM = 100
    var photoUri : Uri? = null
    var photoUrl : String = ""

    private val externalStorageRQ : Int = 1
    private val storage : FirebaseStorage = FirebaseStorage.getInstance()

    var memberInfo : MemberInfo? = null
    var customProgressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        loadCurrentUserInfo_toMypage(Source.SERVER)
        customProgressDialog = ProgressDialog(this)

        //수정하기 버튼 눌렀을 때
        ModifyBttn.setOnClickListener {
            onModifyBttnClick()
        }

        //완료 버튼 눌렀을 때
        UpdateBttn.setOnClickListener {
            onUpdateBttnClick()
        }

        //프로필 사진 눌렀을 때
        ProfilePhoto.setOnClickListener {
            //갤러리 보여주기

            when {
                ContextCompat.checkSelfPermission(
                    this,
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


    }

    override fun onStart() {
        super.onStart()
        ProfilePhoto.isEnabled = false  //onCreate에다 하면 설정이 자꾸 해제되어서 여기서 설정해줘야함.
    }

    override fun onResume() {
        super.onResume()
        ProfilePhoto.isEnabled = !ModifyBttn.isEnabled  //갤러리에서 돌아오는 경우에 따른 코드
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    gotoGallery()
                } else {
                    Toast.makeText(
                        baseContext, "프로필 사진을 변경하려면 권한이 필요합니다.\n'설정>앱>문화교회 고등부>권한' 에서 설정해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {  //잘 끝났다고 들으면
            when (requestCode) {
                PICK_IMAGE_FROM_ALBUM -> {
//                    val result = data?.getStringExtra("PROFILE_PHOTO_PATH")
//                    profilePhotoUri = result
                    photoUri = data?.data
//                    ProfilePhoto.setImageURI(photoUri)
                    Glide.with(this).load(photoUri).override(500).into(ProfilePhoto)


                }
            }
        }
    }

    fun gotoGallery(){
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.putExtra("from", "MyPageActivity")
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
    }

    //수정하기 버튼을 누르면
    private fun onModifyBttnClick() {
        UpdateBttn.isEnabled = true
        ModifyBttn.isEnabled = false

        ProfilePhoto.isEnabled = true

        NameEditText.isEnabled = true
        //group그룹은 수정 불가
        EmailEditText_MemUpdate.isEnabled = false
        PhoneEditText.isEnabled = true
        BirthEditText.isEnabled = true
        PostalAddressEditText.isEnabled = true
    }

    //완료버튼을 누르면
    private fun onUpdateBttnClick() {
        //로딩창 띄우기
        customProgressDialog?.show()

        UpdateBttn.isEnabled = false
        ModifyBttn.isEnabled = true

        ProfilePhoto.isEnabled = false

        NameEditText.isEnabled = false
        EmailEditText_MemUpdate.isEnabled = false
        PhoneEditText.isEnabled = false
        BirthEditText.isEnabled = false
        PostalAddressEditText.isEnabled = false

        val email = EmailEditText_MemUpdate.text
        if(photoUri == null){
            val memberInfo = MemberInfo(
                NameEditText.text.toString(),
                GroupEditText.text.toString(),
                EmailEditText_MemUpdate.text.toString(),
                PasswordEditText_Mypage.text.toString(),
                PhoneEditText.text.toString(),
                BirthEditText.text.toString(),
                PostalAddressEditText.text.toString(),
                ""
            )
            setMemberInfo(memberInfo, "MyPageActivity")     //db에 업데이트
        }else {

            val storageRef = storage.reference
            val profileImageRef = storageRef.child("users/" + email + "/profileImage.jpg")
            var uploadTask = profileImageRef.putFile(photoUri!!)
            Log.e("결과", photoUri.toString())
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                        Log.e("에러", it.toString())
                    }
                    memberInfo = null
                }
                profileImageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    photoUrl = task.result.toString()   //url가져오기, 저장
                    memberInfo = MemberInfo(
                        NameEditText.text.toString(),
                        GroupEditText.text.toString(),
                        EmailEditText_MemUpdate.text.toString(),
                        PasswordEditText_Mypage.text.toString(),
                        PhoneEditText.text.toString(),
                        BirthEditText.text.toString(),
                        PostalAddressEditText.text.toString(),
                        photoUrl
                    )
                    setMemberInfo(memberInfo!!, "MyPageActivity")     //db에 업데이트
                    customProgressDialog?.dismiss()

                } else {
                    memberInfo = null
                    Toast.makeText(
                        baseContext, "업로드 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }




    }



}