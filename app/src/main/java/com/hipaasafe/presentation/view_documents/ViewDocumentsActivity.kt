package com.hipaasafe.presentation.view_documents

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityViewDocumentsBinding
import com.hipaasafe.presentation.home_screen.document_fragment.DocumentFragment
import com.hipaasafe.presentation.view_documents.request_document_fragment.RequestDocumentFragment
import com.hipaasafe.utils.PreferenceUtils

class ViewDocumentsActivity : BaseActivity() {
    lateinit var binding: ActivityViewDocumentsBinding
    var chatName:String = ""
    var age:String = ""
    var patientUid:String = ""
    var documentFragment = DocumentFragment.newInstance()
    var requestDocumentFragment = RequestDocumentFragment.newInstance()

    var isRequestedListView:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getIntentData()
        setUpToolbar()
        setFragment(documentFragment)
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            btnRequestedDocument.setOnClickListener {
                setFragment(requestDocumentFragment)
                isRequestedListView = true
                hideNotesView()
            }
        }
    }

    fun hideNotesView(){
        binding.apply {
            layoutMyNotes.visibility = GONE
            btnRequestedDocument.visibility= GONE
        }
    }
    fun showNotesView(){
        binding.apply {
            layoutMyNotes.visibility = VISIBLE
            btnRequestedDocument.visibility= VISIBLE
        }
    }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                chatName = getString(Constants.CometChatConstant.NAME).toString()
                patientUid = getString(Constants.CometChatConstant.UID).toString()
            }
        }
    }

     fun setFragment(fragment: Fragment) {
        binding.apply {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(containerViewDocuments.id, fragment)
            transaction.commit()
        }
    }

    private fun setUpToolbar() {
        binding.toolbarChat.apply {
            tvChatName.text = chatName
            tvLastActive.text = ""
            btnBack.setOnClickListener {
                if (isRequestedListView){
                    setFragment(documentFragment)
                    isRequestedListView = false
                    showNotesView()
                }else{
                    finish()
                }
            }
            toolbarIcon1.visibility = GONE
            toolbarIcon2.visibility = GONE
        }
    }
}