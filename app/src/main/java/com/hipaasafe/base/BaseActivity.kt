package com.hipaasafe.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils

open class BaseActivity:AppCompatActivity() {
    lateinit var preferenceUtils: PreferenceUtils
    var mCurrentPhotoPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, msg:String){
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
    }

    fun toggleFadeView(
        parent: View,
        loader: View,
        imageView: ImageView,
        showLoader: Boolean
    ) {

        if (showLoader) {
            AppUtils.INSTANCE?.hideFadeView(parent, Constants.VIEW_ANIMATE_DURATION)
            AppUtils.INSTANCE?.showFadeView(loader, Constants.VIEW_ANIMATE_DURATION)
            ImageUtils.INSTANCE?.loadLocalGIFImage(imageView, R.drawable.loader2)
            loader.visibility = View.VISIBLE
        } else {
            AppUtils.INSTANCE?.hideView(loader, Constants.VIEW_ANIMATE_DURATION)
            AppUtils.INSTANCE?.showView(parent, Constants.VIEW_ANIMATE_DURATION)
            loader.visibility = View.GONE

        }
    }
}
