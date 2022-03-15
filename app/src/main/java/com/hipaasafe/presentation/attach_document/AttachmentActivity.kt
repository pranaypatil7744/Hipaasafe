package com.hipaasafe.presentation.attach_document

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityAttachmentBinding
import com.hipaasafe.presentation.home_screen.document_fragment.DocumentFragment

class AttachmentActivity : BaseActivity() {
    lateinit var binding:ActivityAttachmentBinding
    var documentFragment = DocumentFragment.newInstance()
    var isForAttachDoc:Boolean = false
    var attachmentSendTo:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttachmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpToolbar()
        setFragment(documentFragment)
    }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                isForAttachDoc = getBoolean(Constants.IsForAttachDoc,false)
                attachmentSendTo = getString(Constants.AttachmentSendTo).toString()
                documentFragment.apply {
                    attachmentShareTo = attachmentSendTo
                    isAttachmentFlow = isForAttachDoc
                }
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
        binding.toolbar.apply {
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            tvDate.visibility = GONE
            tvTitle.text = getString(R.string.documents)
            divider.visibility = VISIBLE
        }
    }
}