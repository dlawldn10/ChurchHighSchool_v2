package com.project.churchschool.Activity

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.churchschool.Fragment.ContentsFragment
import com.project.churchschool.R

class GalleryAdapter(
    private val pathDataSet: ArrayList<String?>?,
    val galleryActivity: Activity,
    val from: String?
) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.GalleryViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false) as CardView
        val galleryViewHolder = GalleryViewHolder(cardView)

        cardView.setOnClickListener {      //갤러리 화면에서 선택한 이미지를 프로필 사진으로 띄워줌.

            val resultIntent = Intent()
            if(from.equals("ContentsFragment")){
                resultIntent.putExtra(
                    "CONTENT_PATH",
                    pathDataSet?.get(galleryViewHolder.adapterPosition).toString()
                )
            }else if(from.equals("MyPageActivity")){
                resultIntent.putExtra(
                    "PROFILE_PHOTO_PATH",
                    pathDataSet?.get(galleryViewHolder.adapterPosition).toString()
                )
            }

            galleryActivity.setResult(Activity.RESULT_OK, resultIntent)
            galleryActivity.finish()
        }

        return galleryViewHolder
    }


    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        var imageView = holder.cardView.findViewById<ImageView>(R.id.imageView)
        Glide.with(galleryActivity).load(pathDataSet?.get(position)).centerCrop().override(500).into(imageView)
    }

    override fun getItemCount() = pathDataSet?.size ?: 0


}