package com.hipaasafe.presentation.web_view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.*
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityWebViewBinding
import com.hipaasafe.utils.isNetworkAvailable

class WebViewActivity : BaseActivity() {
    lateinit var binding: ActivityWebViewBinding
    var url: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpWebView()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnRetry.setOnClickListener {
                setUpWebView()
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            val pdfFile =
                "https://file-examples-com.github.io/uploads/2017/10/file-sample_150kB.pdf"
            val link = getString(Constants.DocumentLink)

            url = "http://docs.google.com/gview?embedded=true&url=$link"
//                "https://drive.google.com/viewerng/viewer?embedded=true&url=$link"
//                "https://docs.google.com/viewer?url=$link"
//                "http://docs.google.com/viewer?url=$link&embedded=true"
//                link.toString()
            setUpToolbar(getString(R.string.view_document))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                webView.apply {
                    webViewClient = MyWebViewClient(this@WebViewActivity)
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.javaScriptEnabled = true
                    settings.pluginState = WebSettings.PluginState.ON
                }
                webView.loadUrl(url)
            } else {
                layoutNoInternet.root.visibility = VISIBLE
            }
        }
    }

    private fun setUpToolbar(screenName: String) {
        binding.toolbar.apply {
            tvTitle.text = screenName
            btnBack.visibility = VISIBLE
            tvDate.visibility = GONE
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    inner class MyWebViewClient(val activity: Activity) : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url.toString()
            view?.loadUrl(url)
            toggleLoader(true)
            return true
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null) {
                view?.loadUrl(url)
                toggleLoader(true)
            }
            return true
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            showToast(error.toString())
            toggleLoader(false)
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            toggleLoader(false)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            toggleLoader(false)
        }
    }
}