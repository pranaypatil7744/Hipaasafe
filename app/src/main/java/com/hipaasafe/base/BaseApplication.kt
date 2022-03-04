package com.hipaasafe.base

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.hipaasafe.Constants
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.di.AppModule
import com.hipaasafe.di.NetworkModule
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.onesignal.OneSignal
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication:Application() {

    companion object{
        lateinit var mContext: Context
        var second = Constants.RESEND_OTP_SECOND
        private var instance: BaseApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
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
        setUpOneSignal()
    }

    private fun setUpOneSignal() {

        OneSignal.initWithContext(this)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.setAppId(Constants.ONE_SIGNAL_APP_ID)
        val id = OneSignal.getDeviceState()?.userId
        OneSignal.setNotificationOpenedHandler { result->
            val data = result?.notification?.additionalData

            data?.let {
                val redirectToType = it.optString("redirectToType")
//                when (redirectToType.toLowerCase()) {
//                    Constants.NotificationType.FRIEND_REQUEST.toLowerCase() -> {
//                        val intent = Intent(
//                            applicationContext(),
//                            InvitationActivity::class.java
//                        )
//
//                        intent.putExtra(Constants.IS_FROM_NOTIFY, true)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        applicationContext().startActivity(intent)
//
//                    }
//                    else -> {
//                        val intent = Intent(
//                            BaseApplication.applicationContext(),
//                            HomeActivity::class.java
//                        )
//                        intent.flags =
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//                        BaseApplication.applicationContext().startActivity(intent)
//                    }
//                }
            }
        }
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