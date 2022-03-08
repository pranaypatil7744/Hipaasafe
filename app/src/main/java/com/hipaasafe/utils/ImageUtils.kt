package com.hipaasafe.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.google.android.material.imageview.ShapeableImageView
import com.hipaasafe.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ImageUtils {
    companion object {
        var INSTANCE: ImageUtils? = null

        fun setImageInstance() {
            if (INSTANCE == null) {
                INSTANCE = ImageUtils()
            }
        }
    }

    fun loadRemoteImage(imageView: ImageView, imageUrl: String?) {
        imageView.rootView?.let { Glide.with(it) }
            ?.load(imageUrl)
            ?.placeholder(R.drawable.ic_defaulf_image)
            ?.error(R.drawable.ic_defaulf_image)
            ?.into(imageView)
    }
    fun saveImage(myBitmap: Bitmap, context: Context): String {
        /**
         * Returns absolute Path of Image
         */
        val pictureFile = createFile(context)
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 30, bytes)
        val byteArray = bytes.toByteArray()
        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(byteArray)
            fos.close()
        } catch (error: Exception) {
            Log.e("Image", "File" + pictureFile.name + "not saved: " + error.message)
        }
        return pictureFile.absolutePath
    }


    fun createFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    fun loadLocalImage(imageView: ImageView?, image: File?) {
        try {
            imageView?.run {
                Glide.with(context)
                    .load(image)
                    .override(width, height)
//                    .placeholder(R.drawable.ic_fanaboard_loader_optimized)
                    .downsample(DownsampleStrategy.CENTER_INSIDE)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(this)
            }
        } catch (e: Exception) {
        }
    }
    fun loadRemoteImageForProfile(imageView: ShapeableImageView, imageUrl: String?) {

        imageView.rootView?.let { Glide.with(it) }
            ?.load(imageUrl)
            ?.placeholder(R.drawable.ic_default_profile_picture)
            ?.error(R.drawable.ic_default_profile_picture)
            ?.into(imageView)
    }

    fun loadRemoteImageForGroupProfile(imageView: ImageView, imageUrl: String?) {
        //Different fn since error and loaing placeholder can differ
        imageView.rootView?.let { Glide.with(it) }
            ?.load(imageUrl)
            ?.placeholder(R.drawable.ic_default_group_picture)
            ?.error(R.drawable.ic_default_group_picture)
            ?.into(imageView)

//        imageView.context?.let {
//            Glide.with(it)
//                .load(imageUrl)
//                .placeholder(R.drawable.ic_default_group_picture)
//                .error(R.drawable.ic_default_group_picture)
//                .into(imageView)
//        }
    }

    fun loadLocalGIFImage(imageView: ImageView, image: Int) {
        try {
            imageView.run {
                Glide.with(context)
                    .asGif()
                    .load(image)
                    .override(width, height)
                    .placeholder(R.drawable.loader)
                    .downsample(DownsampleStrategy.CENTER_INSIDE)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(this)
            }
        } catch (e: Exception) {
        }
    }

    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(
                encodeByte, 0,
                encodeByte.size
            )
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}