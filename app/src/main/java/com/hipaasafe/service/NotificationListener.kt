package com.hipaasafe.service

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hipaasafe.Constants
import com.hipaasafe.utils.PreferenceUtils

class NotificationListener : NotificationListenerService() {

    lateinit var context: Context
    lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate() {
        super.onCreate()
        preferenceUtils = PreferenceUtils(context)
        context = applicationContext
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val pack = sbn?.packageName
        val extras = sbn?.notification?.extras
        val title = extras?.getString("android.title")

        if (pack.equals("com.hipaasafe", ignoreCase = true)) {
            var countString: String? = preferenceUtils.getValue(Constants.NotificationCount)
            if (countString != null) {
                countString = if (countString.equals("", ignoreCase = true)) "0" else countString
                var unReadNotiCount = countString.toInt()
                unReadNotiCount += 1
                preferenceUtils.setValue(Constants.NotificationCount, "" + unReadNotiCount)
            }
        }

        val msgrcv = Intent("Msg")
        msgrcv.putExtra("package", pack)
        msgrcv.putExtra("title", title)
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn!!.packageName.equals("com.hipaasafe", ignoreCase = true)) {
            var countString: String? =
                preferenceUtils.getValue(Constants.NotificationCount)
            if (countString != null) {
                countString = if (countString.equals("", ignoreCase = true)) "0" else countString
                var unReadNotiCount = countString.toInt()
                if (unReadNotiCount > 0) {
                    unReadNotiCount -= 1
                    preferenceUtils.setValue(Constants.NotificationCount, "" + unReadNotiCount)
                }
            }
        }
    }
}