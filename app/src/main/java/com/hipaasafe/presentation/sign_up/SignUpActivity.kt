package com.hipaasafe.presentation.sign_up

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import com.hipaasafe.Constants
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {
    var countryCode: String = ""
    var mobileNo: String = ""
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                mobileNo = getString(Constants.LOGIN_WITH).toString()
                countryCode = getString(Constants.COUNTRY_CODE).toString()
                setUpView()
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            val clist: ArrayList<String> = ArrayList()
            clist.clear()
            clist.add(countryCode)
            val adapterCodes =
                ArrayAdapter<String>(this@SignUpActivity, R.layout.simple_list_item_1, clist)
            binding.spinnerCountryCode.adapter = adapterCodes

            etMobile.setText(mobileNo)
        }
    }
}