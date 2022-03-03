package com.hipaasafe.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityMainBinding
import com.hipaasafe.presentation.login.model.CountryModel
import com.hipaasafe.presentation.verify_otp.VerifyOtpActivity
import com.hipaasafe.utils.AppUtils

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    var countryList: ArrayList<CountryModel> = ArrayList()

    var isDoctorLogin: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setUpListener()
        setUpCountryCodes()
    }

    private fun getIntentData() {
        binding.apply {
            intent.extras?.run {
                isDoctorLogin = getBoolean(Constants.IS_DOCTOR_LOGIN, false)
                setUpView()
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            if (isDoctorLogin) {
                setDoctorLoginView()
            } else {
                setPatientLoginView()
            }
        }
    }

    private fun setPatientLoginView() {
        binding.apply {
            imgMain.setImageResource(R.drawable.img_login_patient)
            layoutEmail.visibility = INVISIBLE
            layoutMobile.visibility = VISIBLE
            btnContinue.text = getString(R.string.sent_otp)
            tvByClicking.visibility = VISIBLE
            tvTermCondition.visibility = VISIBLE
        }
    }

    private fun setDoctorLoginView() {
        binding.apply {
            imgMain.setImageResource(R.drawable.img_login_doctor)
            layoutEmail.visibility = VISIBLE
            layoutMobile.visibility = INVISIBLE
            btnContinue.text = getString(R.string._continue)
            tvByClicking.visibility = INVISIBLE
            tvTermCondition.visibility = INVISIBLE
        }
    }

    private fun setUpCountryCodes() {

        countryList.addAll(AppUtils.INSTANCE?.getCountriesList(this) ?: ArrayList())
//        val adapterCodes =
//            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryList.map { it ->
//                it.dial_code
//            })
        val clist: ArrayList<String> = ArrayList()
        clist.add("+91")
        clist.add("+92")
        clist.add("+93")
        clist.add("+94")
        clist.add("+95")
        val adapterCodes =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clist)
        binding.spinnerCountryCode.adapter = adapterCodes
        val usIndex = AppUtils.INSTANCE?.getUSIndex(countryList) ?: 0

        if (usIndex > 0) {
            binding.spinnerCountryCode.setSelection(usIndex)
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnContinue.setOnClickListener {
                val i = Intent(this@LoginActivity, VerifyOtpActivity::class.java)
                val b = Bundle()
                b.putBoolean(Constants.IS_DOCTOR_LOGIN, isDoctorLogin)
                if (isDoctorLogin) {
                    b.putString(Constants.LOGIN_WITH, etEmail.text.toString().trim())
                } else {
                    b.putString(Constants.LOGIN_WITH, etMobile.text.toString().trim())
                }
                i.putExtras(b)
                startActivity(i)
            }
        }
    }

}