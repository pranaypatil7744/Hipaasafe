package com.hipaasafe.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivitySplashBinding
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.login_main.LoginMainActivity
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        preferenceUtils = PreferenceUtils(this)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        ImageUtils.INSTANCE?.loadLocalGIFImage(binding.icSplashLogo, R.drawable.ic_hipaasafe_gif)

        Handler(mainLooper).postDelayed({
            val isLogin = preferenceUtils.getValue(Constants.IS_LOGIN, false)
            if (isLogin) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginMainActivity::class.java))
            }
        }, 3000)

    }
}
