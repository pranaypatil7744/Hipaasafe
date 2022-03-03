package com.hipaasafe.presentation.sign_up

import android.os.Bundle
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {
    lateinit var binding:ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}