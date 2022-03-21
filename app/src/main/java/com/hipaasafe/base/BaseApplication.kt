package com.hipaasafe.base

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.geniusscansdk.core.GeniusScanSDK
import com.geniusscansdk.core.LicenseException
import com.hipaasafe.AppConfig
import com.hipaasafe.Constants
import com.hipaasafe.Constants.Companion.GENIUS_SDK_LICENSE
import com.hipaasafe.R
import com.hipaasafe.di.AppModule
import com.hipaasafe.di.NetworkModule
import com.hipaasafe.listener.CometChatCallListener
import com.hipaasafe.presentation.home_screen.HomeActivity
import com.hipaasafe.presentation.notification.NotificationActivity
import com.hipaasafe.settings.CometChatSettings
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.TagName
import com.onesignal.OneSignal
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication : Application(), LifecycleObserver, Application.ActivityLifecycleCallbacks {

    companion object {
        lateinit var mContext: Context
        var second = Constants.RESEND_OTP_SECOND
        var instance: BaseApplication? = null
        const val TAG = "UIKitApplication"
        var isBackground = false
        var isForeground = false

        lateinit var preferenceUtils: PreferenceUtils
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
        preferenceUtils = PreferenceUtils(mContext)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        try {
            GeniusScanSDK.init(this, GENIUS_SDK_LICENSE)
        } catch (e: LicenseException) {
            // The license is expired or invalid
        }
        setUpOneSignal()
        setUpCometChat()
    }

    private fun setUpCometChat() {
        val appSettings = AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers()
            .setRegion(AppConfig.AppDetails.REGION)
            .autoEstablishSocketConnection(true)
            .build()
        CometChat.init(this, AppConfig.AppDetails.APP_ID, appSettings, object :
            CometChat.CallbackListener<String>() {
            override fun onSuccess(p: String?) {
                /*** Comet Chat Initialization successfully ****/
                AppUtils.INSTANCE?.logMe(TagName.COMET_SDK, "Initialization completed successfully")

            }

            override fun onError(e: CometChatException?) {
                /*** Comet Chat Initialization failure ****/
                AppUtils.INSTANCE?.logMe(
                    TagName.COMET_SDK,
                    "Initialization failed with exception: " + e?.message
                )
            }
        })
        val cometChatSettings = CometChatSettings(this)
        cometChatSettings.addConnectionListener(TagName.COMET_SDK)
        CometChatCallListener.addCallListener(TagName.COMET_SDK, this)
        createNotificationChannel()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
        val isMuteNotification = preferenceUtils.getValue(Constants.PreferenceKeys.mute_notifications, false)
        CometChatSettings.enableSoundForCalls = !isMuteNotification
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("2", name, importance)
            channel.description = description
            channel.enableVibration(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setUpOneSignal() {

        OneSignal.initWithContext(this)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.setAppId(Constants.ONE_SIGNAL_APP_ID)
        val id = OneSignal.getDeviceState()?.userId
        OneSignal.setNotificationOpenedHandler { result ->
            val data = result?.notification?.additionalData

            data?.let {
                val redirectToType = it.optString("redirectToType")
                val intent = Intent(
                    applicationContext(),
                    NotificationActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext().startActivity(intent)
                when (redirectToType.toLowerCase()) {
                    Constants.NotificationType.FRIEND_REQUEST.toLowerCase() -> {
                        val intent = Intent(
                            applicationContext(),
                            NotificationActivity::class.java
                        )

//                        intent.putExtra(Constants.IS_FROM_NOTIFY, true)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext().startActivity(intent)

                    }
                    else -> {
                        val intent = Intent(
                            applicationContext(),
                            HomeActivity::class.java
                        )
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationContext().startActivity(intent)
                    }
                }
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

    fun onMoveToForeground() {
        isBackground = false
        isForeground = true
    }

    fun onMoveToBackground() {
        isBackground = true
        isForeground = false
    }

    override fun onTerminate() {
        super.onTerminate()
        isForeground = false
        CometChatCallListener.removeCallListener(TagName.COMET_SDK)
        CometChat.removeConnectionListener(TagName.COMET_SDK)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
//        isForeground = true
    }

    override fun onActivityStarted(p0: Activity) {
//        isForeground = true

    }

    override fun onActivityResumed(p0: Activity) {
        isForeground = true

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {
        isForeground = false

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {
        isForeground = false
    }
}