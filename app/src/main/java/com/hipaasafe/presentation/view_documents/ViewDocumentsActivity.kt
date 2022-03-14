package com.hipaasafe.presentation.view_documents

import android.os.Bundle
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityViewDocumentsBinding
import com.hipaasafe.utils.PreferenceUtils

class ViewDocumentsActivity : BaseActivity() {
    lateinit var binding: ActivityViewDocumentsBinding
    var chatName:String = ""
    var age:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getIntentData()
        setUpToolbar()
    }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                chatName = getString(Constants.CometChatConstant.NAME).toString()
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbarChat.apply {
            tvChatName.text = chatName
            tvLastActive.text = ""
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}