package com.hipaasafe.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.hipaasafe.R
import java.io.ByteArrayOutputStream


class ImageUtils {
    companion object {
        var INSTANCE: ImageUtils? = null

        fun setImageInstance() {
            if (INSTANCE == null) {
                INSTANCE = ImageUtils()
            }
        }
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