package com.project.churchschool.Activity

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.churchschool.R

class GalleryActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val numberOfColumns = 3
        var recyclerView: RecyclerView = findViewById(R.id.Gallery_recycler_view)

        viewManager = LinearLayoutManager(this)
        viewAdapter = GalleryAdapter(getImagesPath(this), this, intent.getStringExtra("from") )     //어댑터 생성
        recyclerView = findViewById<RecyclerView>(R.id.Gallery_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter   //어댑터 연결
            recyclerView.layoutManager = GridLayoutManager(this@GalleryActivity, numberOfColumns)
        }
    }

    private fun getImagesPath(activity: Activity) : ArrayList<String?>{
        val uri: Uri
        val listOfAllImages = ArrayList<String?>()
        val cursor: Cursor?
        val column_index_data: Int
//        val column_index_folder_name: Int
        var PathOfImage: String? = null

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        cursor = activity.contentResolver.query(uri, projection, null, null, null)

        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(PathOfImage)
        }
        return listOfAllImages
    }
}