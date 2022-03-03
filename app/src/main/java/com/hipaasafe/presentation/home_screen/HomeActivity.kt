package com.hipaasafe.presentation.home_screen

import android.os.Bundle
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}