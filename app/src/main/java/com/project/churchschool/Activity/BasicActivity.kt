package com.project.churchschool.Activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Source
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

open class BasicActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    var StudentsAttndnceData : MutableMap<String, Boolean>? = mutableMapOf()
    var SelectedMemo : MemoData? = null
    var SelectedAttndnce : AttndnceData? = null
    var SelectedAttndnce_ : QR_AttendanceData? = null
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
        db.firestoreSettings = settings     //오프라인일때 캐시 사용하여 로드하기 설정.


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
//                val fragMyClassAttendance = MyclassAttendanceFragment()
//                fragmentTransaction.replace(Container_frameLayout.id, fragMyClassAttendance)
//                fragmentTransaction.commit()
                if (currentUserInfo?.group == "학생") {
                    val fragCreateQR = QRcodeCreaterFragment()
                    fragmentTransaction.replace(Container_frameLayout.id, fragCreateQR)
                    fragmentTransaction.commit()
                }else{
//                    gotoQRscannerActivity(this)
                    val fragScanQR = QRcodeAttendanceFragment()
                    fragmentTransaction.replace(Container_frameLayout.id, fragScanQR)
                    fragmentTransaction.commit()
                }


//                val fragScanQR = QRcodeScannerFragment()
//                fragmentTransaction.replace(Container_frameLayout.id, fragScanQR)
//                fragmentTransaction.commit()
//                gotoQRscannerActivity(this)
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
//                val TchrFragAttndnce = Teacher_AttndnceChckFragment()
//                fragmentTransaction.replace(Container_frameLayout.id, TchrFragAttndnce)
//                fragmentTransaction.commit()

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
                val TchrFragAttndnce = AttendanceCloserLookFragment()
                fragmentTransaction.replace(Container_frameLayout.id, TchrFragAttndnce)
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
        setSupportActionBar(main_layout_toolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) // 홈버튼(툴바 왼쪽 끝 버튼) 이미지 변경
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true) // 툴바에 타이틀 보이게

        main_navigationView.setNavigationItemSelectedListener(this) //navigation 리스너
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //메뉴
            R.id.Mypage -> gotoMypageActivity(this)

            R.id.StudentList -> Toast.makeText(this, "학생수첩", Toast.LENGTH_SHORT).show()

            R.id.TeacherList -> Toast.makeText(this, "교사수첩", Toast.LENGTH_SHORT).show()

            R.id.LogOut -> logOut()



            //하단바
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
            android.R.id.home->{ // = 메뉴 버튼
                main_drawer_layout.openDrawer(GravityCompat.START)    // 네비게이션 드로어 열기
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()
        gotoLogInActivity(this)
        Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
        finish()
    }


    fun GroupCheck_SignUp() : Boolean{     //교사/학생 항목 중 하나를 체크했는지 확인.
        var result :Boolean = false
        if(StudentRadioBttn_Signup.isChecked || TeacherRadioBttn_Signup.isChecked){
            result = true
        }

        return result
    }

    fun GroupCheck_Login() : Boolean{     //교사/학생 항목 중 하나를 체크했는지 확인.
        var result :Boolean = false
        if(StudentRadioBttn_Login.isChecked || TeacherRadioBttn_Login.isChecked){
            result = true
        }

        return result
    }

    //MyPage에서 쓰이는 함수 : 현재 로그인 한 회원 정보 가져오기
    fun loadCurrentUserInfo_toMypage(from : Source) {
        val user = auth.currentUser
        val currentUser = db.collection("users").document(user?.email.toString()) //이거 전역변수로 빼지 않기
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


    //회원가입 및 회원정보 업데이트 시 호출
    fun setMemberInfo(memberInfo : MemberInfo, activityName: String){
        val user = auth.currentUser
        val currentUser = db.collection("users").document(memberInfo.email.toString())
        currentUser.set(memberInfo).addOnSuccessListener {

            Log.e("결과", "db등록 성공")
            if(activityName.equals("MyPageActivity")){
                Toast.makeText(
                    baseContext, "업데이트가 완료되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if(activityName.equals("SignUpActivity")){
                Toast.makeText(
                    baseContext, "2222",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }   //db에 회원 등록.
    }

    //출석정보 리스트로 저장
    fun saveAttndnceData(name : String, Attndnce : Boolean){
        this.StudentsAttndnceData?.put(name, Attndnce)

    }

    //출석정보 업데이트 시 호출
    fun addAttndnceInfo(db : FirebaseFirestore, currentUserInfo : MemberInfo, FinalattndnceData : AttndnceData){

        db.collection("attendances")
            .document(currentUserInfo.name.toString())
            .update("attndnceDataList", FieldValue.arrayUnion(FinalattndnceData))
            .addOnSuccessListener {

                Log.e("결과", "db등록 성공")
                Toast.makeText(
                    baseContext, "출석정보가 업데이트 되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                setView(2)
        }   //db에 회원 등록.

    }

    fun convertTime(): Date {
        val now : Long = System.currentTimeMillis() //현재시간 가져오기
        val mDate = Date(now)   //Date형식으로 바꾸기
        return mDate
    }

    fun getYear() : String{
        val year = SimpleDateFormat("YYYY")
        val getyear: String = year.format(convertTime())
        return getyear
    }

    fun getMonth(): String {
        val month = SimpleDateFormat("MM")     //가져오고싶은 형태 지정
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

//        val dateFormat = SimpleDateFormat("YYYY/MM/DD")
//        val getYYYYMMDD_Date : String = dateFormat.format(convertTime())
        return df.format(cal.time)

    }

    fun getYYYMM() : String{
        val dateFormat = SimpleDateFormat("YYYY/MM/")
        val getYYYYMM_Date : String = dateFormat.format(convertTime())
        return getYYYYMM_Date

    }


    //액티비티 이동 함수
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

    fun gotoGalleryActivity(activity : Activity){
        val intent = Intent(activity, GalleryActivity::class.java)
        startActivity(intent)
    }

    fun gotoQRscannerActivity(activity : Activity){
        val intent = Intent(activity, QRcodeScannerActivity::class.java)
        startActivity(intent)
    }
}