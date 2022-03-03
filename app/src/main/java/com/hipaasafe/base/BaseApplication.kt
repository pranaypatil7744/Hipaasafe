package com.hipaasafe.base

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.hipaasafe.Constants
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.di.AppModule
import com.hipaasafe.di.NetworkModule
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication:Application() {

    companion object{
        lateinit var mContext: Context
        var second = Constants.RESEND_OTP_SECOND

    }
    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        MultiDex.install(this)
        initSingleton()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(listOf(AppModule, NetworkModule))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    fun getAppContext(): Context {
        return mContext
    }

    private fun initSingleton() {
        AppUtils.setInstance()
        AppUtils.INSTANCE?.preferenceUtils = PreferenceUtils(this)
        ImageUtils.setImageInstance()
    }
}