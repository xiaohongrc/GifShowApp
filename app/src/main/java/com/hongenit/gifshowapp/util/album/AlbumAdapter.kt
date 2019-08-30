package com.hongenit.gifshowapp.util.album

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.extension.showToast
import cropper.CropImage
import cropper.CropImageView
import java.io.File

/**
 * 相册的适配器，在这里处理图片的缩略图展示，以及选中图片的逻辑。
 *
 */
open class AlbumAdapter() : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    var mContext: Context? = null

    var mImageList: List<String> = ArrayList()

    protected var mImageSize: Int = 0

    private var cropWidth: Int = 0

    private var cropHeight: Int = 0

    constructor(cropWidth: Int, cropHeight: Int) : this() {
        this.cropWidth = cropWidth
        this.cropHeight = cropHeight
    }

    /**
     * 将相册中的图片列表传入。
     *
     * @param imageList
     * 相册中图片路径的列表
     */
    fun setImageList(imageList: List<String>) {
        mImageList = imageList
    }

    /**
     * 将每张图片的尺寸传入。
     *
     * @param imageSize
     * 图片的尺寸
     */
    fun setImageSize(imageSize: Int) {
        mImageSize = imageSize
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder {
        if (mContext == null) {
            mContext = parent.context
        }
        val view = LayoutInflater.from(mContext).inflate(R.layout.album_image_item, parent, false)
        val holder = ViewHolder(view)
        holder.image.setOnClickListener {
            if (mContext is Activity) {
                val activity = mContext as Activity
                val position = holder.adapterPosition
                val imagePath = mImageList[position]
                val fileUri = Uri.fromFile(File(imagePath))
                CropImage.activity(fileUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setFixAspectRatio(true)
                    .setAspectRatio(cropWidth, cropHeight)
                    .setActivityTitle(mContext!!.getString(R.string.crop))
                    .setRequestedSize(cropWidth, cropHeight)
                    .setCropMenuCropButtonIcon(R.drawable.ic_crop)
                    .start(activity)
            } else {
                mContext?.let {
                    showToast(it.getString(R.string.unknown_error))
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int) {
        holder.image.layoutParams.width = mImageSize
        holder.image.layoutParams.height = mImageSize
        val imagePath = mImageList[position]
//        if (ImageUtil.isGif(imagePath)) {
//            Glide.with(mContext!!)
//                .load(imagePath)
//                .placeholder(R.drawable.album_loading_bg)
//                .override(mImageSize, mImageSize)
//                .into(holder.image)
//        } else {
        Glide.with(mContext!!)
            .load(imagePath)
            .placeholder(R.drawable.album_loading_bg)
            .override(mImageSize, mImageSize)
            .into(holder.image)
//        }
    }

    override fun getItemCount() = mImageList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /**
         * 用于展示缩略图的ImageView
         */
        var image: ImageView = view.findViewById(R.id.albumImage)

    }

    companion object {

        private const val TAG = "AlbumAdapter"
    }

}
