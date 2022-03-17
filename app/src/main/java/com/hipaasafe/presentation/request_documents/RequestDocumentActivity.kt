package com.hipaasafe.presentation.request_documents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityRequestDocumentBinding
import com.hipaasafe.presentation.view_documents.request_document_fragment.RequestDocumentFragment

class RequestDocumentActivity : BaseActivity() {
    var requestDocumentFragment = RequestDocumentFragment.newInstance()
    lateinit var binding:ActivityRequestDocumentBinding
    var chatName: String = ""
    var guid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpToolbar()
        setFragment(requestDocumentFragment)
    }
    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                chatName = getString(Constants.CometChatConstant.NAME).toString()
                guid = getString(Constants.CometChatConstant.GUID).toString()
            }
        }
    }


    fun setFragment(fragment: Fragment) {
        binding.apply {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(containerRequestDocuments.id, fragment)
            transaction.commit()
        }
    }

    private fun setUpToolbar() {
        binding.toolbarChat.apply {
            tvChatName.text = chatName
            tvLastActive.text = ""
            btnBack.setOnClickListener {
               finish()
            }
            toolbarIcon1.visibility = View.GONE
            toolbarIcon2.visibility = View.GONE
        }
    }
}