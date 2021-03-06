package com.project.churchschool.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.DataClass.MemoData
import com.project.churchschool.DataClass.QR_AttendanceData
import com.project.churchschool.Fragment.*
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.main_drawer_header.view.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

open class BasicActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    var StudentsAttndnceData : MutableMap<String, Boolean>? = mutableMapOf()
    var SelectedMemo : MemoData? = null
    var SelectedAttndnce : AttndnceData? = null
    var SelectedAttndnce_ : QR_AttendanceData? = null
    var SelectedStudent : String = ""
    var myDBname = ""

    private val db = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = Firebase.auth
    private val user = auth.currentUser
    val currentUser = db.collection("users").document(user?.email.toString())
    var currentUserInfo : MemberInfo? = null


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings     //?????????????????? ?????? ???????????? ???????????? ??????.

    }



    fun getUser() : FirebaseUser? {
        return user
    }

    fun setBottomNavBar(){
        val navView: BottomNavigationView = findViewById(R.id.BottomNavi)
        navView.setOnNavigationItemSelectedListener(this)

    }


    fun setView(n: Int){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when(n){
            1->{
                val fragHome = HomeFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragHome)
                fragmentTransaction.commit()
            }
            2->{
                if (currentUserInfo?.group == "??????") {
                    val fragCreateQR = QRcodeCreaterFragment()
                    fragmentTransaction.replace(Container_frameLayout.id, fragCreateQR)
                    fragmentTransaction.addToBackStack("attendance")
                    fragmentTransaction.commit()
                }else{
                    val fragAttendance = QRcodeAttendanceFragment()
                    fragmentTransaction.replace(Container_frameLayout.id, fragAttendance)
                    fragmentTransaction.addToBackStack("attendance")
                    fragmentTransaction.commit()
                }

            }
            3->{
                val fragContents = ContentsFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragContents)
                fragmentTransaction.commit()
            }
            4->{
                val fragMemo = MemoFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragMemo)
                fragmentTransaction.commit()
            }
            5->{
                val fragPrayer = PrayerListFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragPrayer)
                fragmentTransaction.commit()
            }
            6->{
                gotoQRscannerActivity(this)
            }
            7->{
                val fragPostMemo = MemoPostFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragPostMemo)
                fragmentTransaction.commit()
            }
            8->{
                val fragPostPrayer = PrayerPostFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragPostPrayer)
                fragmentTransaction.commit()
            }
            9->{
                val fragAttendanceCloserLook = AttendanceCloserLookFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragAttendanceCloserLook)
                fragmentTransaction.addToBackStack("attendance")
                fragmentTransaction.commit()
            }
            10->{
                val fragStudentList = StudentListFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragStudentList)
                fragmentTransaction.addToBackStack("attendance")
                fragmentTransaction.commit()
            }
        }
    }


    fun setSelectedMemoData(data : MemoData?){
        SelectedMemo = data
    }

    fun getSelectedMemoData() : MemoData? {
        return SelectedMemo
    }

    fun setSelectedAttendnceData(data : AttndnceData?){
        SelectedAttndnce = data
    }

    fun setSelectedAttendnceDataList(data : QR_AttendanceData){
        SelectedAttndnce_ = data
    }

    fun setSelectedStudentName(data : String){
        SelectedStudent = data  //?????? ??? ???????????? ????????? ????????? ?????????????????? ????????? ????????? ??????????????? //??? ?????? ????????? ????????????..
        val year = SelectedAttndnce_!!.date?.slice(0..3)
        val month = SelectedAttndnce_!!.date?.slice(5..6)
        val date = SelectedAttndnce_!!.date
        val newData = hashMapOf(data + " ??????" to true)
        db.collection("attendances").document(year.toString()).collection(month.toString()).document(date.toString())
            .set(newData, SetOptions.merge())
    }

    fun delete_SelectedStudentName(data : String){
        SelectedStudent = data  //?????? ??? ???????????? ????????? ????????? ?????????????????? ????????? ????????? ??????????????? //??? ?????? ????????? ????????????..
        val year = SelectedAttndnce_!!.date?.slice(0..3)
        val month = SelectedAttndnce_!!.date?.slice(5..6)
        val date = SelectedAttndnce_!!.date
        val updates = hashMapOf<String, Any>(
            data to FieldValue.delete()
        )

        db.collection("attendances").document(year.toString()).collection(month.toString())
            .document(date.toString()).update(updates).addOnCompleteListener {
                Toast.makeText(this, "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show()
            }

    }

    fun getSelectedAttendnceData() : AttndnceData? {
        return SelectedAttndnce
    }

    fun getSelectedAttendnceDataList() : QR_AttendanceData? {
        return SelectedAttndnce_
    }

    fun set_myDBname(name : String){
        myDBname = name
    }

    fun get_myDBname() : String{
        return myDBname
    }

    fun setToolbar(){
        setSupportActionBar(main_layout_toolbar) // ????????? ??????????????? ????????? ??????
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) // ?????????(?????? ?????? ??? ??????) ????????? ??????
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ???????????? ?????? ??? ?????? ?????????
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true) // ????????? ????????? ?????????

        main_navigationView.setNavigationItemSelectedListener(this) //navigation ?????????
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //??????
            R.id.Mypage -> gotoMypageActivity(this)

            R.id.StudentList -> Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()

            R.id.TeacherList -> Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()

            R.id.LogOut -> logOut()



            //?????????
            R.id.Home -> {
                setView(1)
            }
            R.id.AttendanceCheck -> {
                setView(2)
            }
            R.id.Contents -> {
                setView(3)
            }
            R.id.Memo -> {
                setView(4)
            }
            R.id.PrayerList -> {
                setView(5)
            }

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ // = ?????? ??????
                main_drawer_layout.openDrawer(GravityCompat.START)    // ??????????????? ????????? ??????
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()
        gotoLogInActivity(this)
        Toast.makeText(this, "???????????? ???????????????", Toast.LENGTH_SHORT).show()
        finish()
    }


    fun GroupCheck_SignUp() : Boolean{     //??????/?????? ?????? ??? ????????? ??????????????? ??????.
        var result :Boolean = false
        if(StudentRadioBttn_Signup.isChecked || TeacherRadioBttn_Signup.isChecked){
            result = true
        }

        return result
    }

    fun GroupCheck_Login() : Boolean{     //??????/?????? ?????? ??? ????????? ??????????????? ??????.
        var result :Boolean = false
        if(StudentRadioBttn_Login.isChecked || TeacherRadioBttn_Login.isChecked){
            result = true
        }

        return result
    }

    //MyPage?????? ????????? ?????? : ?????? ????????? ??? ?????? ?????? ????????????
    fun loadCurrentUserInfo_toMypage(from : Source) {
        val user = auth.currentUser
        val currentUser = db.collection("users").document(user?.email.toString()) //?????? ??????????????? ?????? ??????
        currentUser.get(from).addOnSuccessListener { documentSnapshot ->
            currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)
            NameEditText.setText(currentUserInfo?.name)
            GroupEditText.setText(currentUserInfo?.group)
            EmailEditText_MemUpdate.setText(currentUserInfo?.email)
            PasswordEditText_Mypage.setText(currentUserInfo?.password)
            PhoneEditText.setText(currentUserInfo?.phone)
            BirthEditText.setText(currentUserInfo?.birth)
            PostalAddressEditText.setText(currentUserInfo?.postalAddress)
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500).into(ProfilePhoto)
        }

    }

    fun setDrawer(headerView : View){
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)
            headerView.drawer_Name.setText(currentUserInfo?.name + " " + currentUserInfo?.group)
            headerView.drawer_email.setText(currentUserInfo?.email)
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500).into(headerView.header_icon)
        }
    }

    fun updateDrawer(headerView : View){
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)
            headerView.drawer_Name.setText(currentUserInfo?.name + " " + currentUserInfo?.group)
            headerView.drawer_email.setText(currentUserInfo?.email)
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500).into(headerView.header_icon)
        }
    }



    //???????????? ??? ???????????? ???????????? ??? ??????
    fun setMemberInfo(memberInfo : MemberInfo, activityName: String){
        val user = auth.currentUser
        val currentUser = db.collection("users").document(memberInfo.email.toString())
        currentUser.set(memberInfo).addOnSuccessListener {

//            Log.e("??????", "db?????? ??????")
            if(activityName.equals("MyPageActivity")){
                Toast.makeText(
                    baseContext, "??????????????? ?????????????????????.",
                    Toast.LENGTH_SHORT
                ).show()

            }
            if(activityName.equals("SignUpActivity")){
                Toast.makeText(
                    baseContext, "??????????????? ?????????????????????.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }   //db??? ?????? ??????.
    }


    fun convertTime(): Date {
        val now : Long = System.currentTimeMillis() //???????????? ????????????
        val mDate = Date(now)   //Date???????????? ?????????
        return mDate
    }

    fun getYear() : String{
        val year = SimpleDateFormat("YYYY")
        val getyear: String = year.format(convertTime())
        return getyear
    }

    fun getMonth(): String {
        val month = SimpleDateFormat("MM")     //?????????????????? ?????? ??????
        val getmonth: String = month.format(convertTime())
        return getmonth
    }

    fun getDate() : String{

        return Calendar.DATE.toString()
    }

    fun getYYYYMMDD() : String{

        val cal = Calendar.getInstance()
        cal.time = Date()
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        return df.format(cal.time)

    }


    //???????????? ?????? ??????
    fun gotoMainActivity(activity : Activity){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    fun gotoSignUpActivity(activity : Activity){
        val intent = Intent(activity, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun gotoLogInActivity(activity : Activity){
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    fun gotoPasswordRecreateActivity(activity : Activity){
        val intent = Intent(activity, PasswordRecreateActivity::class.java)
        startActivity(intent)
    }

    fun gotoMypageActivity(activity : Activity){
        val intent = Intent(activity, MyPageActivity::class.java)
        startActivity(intent)
    }

    fun gotoQRscannerActivity(activity : Activity){
        val intent = Intent(activity, QRcodeScannerActivity::class.java)
        startActivity(intent)
    }
}