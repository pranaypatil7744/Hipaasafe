package com.hipaasafe.presentation.login_main

import android.content.Intent
import android.os.Bundle
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityLoginMainBinding
import com.hipaasafe.presentation.login.LoginActivity

class LoginMainActivity : BaseActivity() {
    lateinit var binding: ActivityLoginMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            btnDoctorLogin.setOnClickListener {
                val i = Intent(this@LoginMainActivity, LoginActivity::class.java)
                val b = Bundle()
                b.putBoolean(Constants.IS_DOCTOR_LOGIN, true)
                i.putExtras(b)
                startActivity(i)
            }
            btnPatientLogin.setOnClickListener {
                val i = Intent(this@LoginMainActivity, LoginActivity::class.java)
                val b = Bundle()
                b.putBoolean(Constants.IS_DOCTOR_LOGIN, false)
                i.putExtras(b)
                startActivity(i)
            }
        }
    }
}