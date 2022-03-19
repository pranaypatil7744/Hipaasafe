package com.hipaasafe.presentation.image_viewer

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityImageViewerBinding
import com.hipaasafe.utils.ImageUtils

class ImageViewerActivity : BaseActivity() {
    lateinit var binding:ActivityImageViewerBinding
    var docLink:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpToolbar()
    }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                docLink = getString(Constants.DocumentLink).toString()
                setUpView()
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            ImageUtils.INSTANCE?.loadRemoteImage(imgDocument,docLink)
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.view_document)
            tvDate.visibility = GONE
            divider.visibility = VISIBLE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}