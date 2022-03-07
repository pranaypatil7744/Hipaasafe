package com.hipaasafe.presentation.main_chat

import android.os.Bundle
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityMainCometChatBinding

class MainCometChatActivity : BaseActivity() {
    lateinit var binding:ActivityMainCometChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCometChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}