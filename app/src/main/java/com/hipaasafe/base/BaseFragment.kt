package com.hipaasafe.base

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils

open class BaseFragment:Fragment() {
    lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
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
            ImageUtils.INSTANCE?.loadLocalGIFImage(
                imageView,
                R.drawable.loader2
            )
            loader.visibility = View.VISIBLE
        } else {
            AppUtils.INSTANCE?.hideView(loader, Constants.VIEW_ANIMATE_DURATION)
            AppUtils.INSTANCE?.showView(parent, Constants.VIEW_ANIMATE_DURATION)
            loader.visibility = View.GONE

        }
    }
}