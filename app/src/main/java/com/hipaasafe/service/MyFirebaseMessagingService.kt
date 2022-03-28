package com.hipaasafe.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.helpers.CometChatHelper
import com.cometchat.pro.models.BaseMessage
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.MediaMessage
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hipaasafe.AppConfig
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseApplication
import com.hipaasafe.presentation.comet_chat_main_screen.MainCometChatActivity
import com.hipaasafe.utils.PreferenceUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseService"
    private var json: JSONObject? = null
    private val intent: Intent? = null
    private var count = 0
    private var call: Call? = null
    var token: String? = null
    private val REQUEST_CODE = 12
//    private val PENDING_REQUEST_CODE = 0123

    private var isCall = false
    lateinit var preferenceUtils: PreferenceUtils

    fun subscribeUserNotification(UID: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(
            AppConfig.AppDetails.APP_ID + "_" + CometChatConstants.RECEIVER_TYPE_USER + "_" +
                    UID
        ).addOnSuccessListener {

        }
    }

    fun unsubscribeUserNotification(UID: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(
            (AppConfig.AppDetails.APP_ID + "_" + CometChatConstants.RECEIVER_TYPE_USER + "_" +
                    UID)
        ).addOnSuccessListener {

        }
    }

    fun subscribeGroupNotification(GUID: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(
            (AppConfig.AppDetails.APP_ID + "_" + CometChatConstants.RECEIVER_TYPE_GROUP + "_" +
                    GUID)
        ).addOnSuccessListener {

        }
    }

    fun unsubscribeGroupNotification(GUID: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(
            (AppConfig.AppDetails.APP_ID + "_" + CometChatConstants.RECEIVER_TYPE_GROUP + "_" +
                    GUID)
        )
    }

    override fun onNewToken(t: String) {
        super.onNewToken(t)
        preferenceUtils = PreferenceUtils(this)
        preferenceUtils.setValue(Constants.FIREBASE_TOKEN, t)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //TODO handle notification
        super.onMessageReceived(remoteMessage)

        try {
            count++
            json = JSONObject(remoteMessage.data as Map<*, *>)
            val messageData = JSONObject(json?.getString("message"))
            val baseMessage =
                CometChatHelper.processMessage(JSONObject(remoteMessage.data["message"]))
            if (baseMessage is Call) {
                call = baseMessage
                isCall = true
            }
            showNotification(baseMessage, messageData.getString("receiver"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getBitmapFromURL(strURL: String?): Bitmap? {
        return if (strURL != null) {
            try {
                val url = URL(strURL)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input: InputStream = connection.getInputStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }


    private fun showNotification(baseMessage: BaseMessage, receiverId: String) {
        try {
            val GROUP_ID = "group_id"

            val messageIntent = Intent(
                applicationContext,
                MainCometChatActivity::class.java
            )
            messageIntent.putExtra(Constants.CometChatConstant.TYPE, baseMessage.receiverType)

            messageIntent.setAction("android.intent.action.MAIN")
            messageIntent.addCategory("android.intent.category.LAUNCHER")
            messageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            messageIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)

            if (baseMessage.receiverType.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                messageIntent.putExtra(Constants.CometChatConstant.NAME, baseMessage.sender.name)
                messageIntent.putExtra(Constants.CometChatConstant.UID, baseMessage.sender.uid)
                messageIntent.putExtra(
                    Constants.CometChatConstant.AVATAR,
                    baseMessage.sender.avatar
                )
                messageIntent.putExtra(
                    Constants.CometChatConstant.STATUS,
                    baseMessage.sender.status
                )
            } else if (baseMessage.receiverType.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                messageIntent.putExtra(
                    Constants.CometChatConstant.GUID,
                    (baseMessage.receiver as Group).guid
                )
                messageIntent.putExtra(
                    Constants.CometChatConstant.NAME,
                    (baseMessage.receiver as Group).name
                )
                messageIntent.putExtra(
                    Constants.CometChatConstant.GROUP_DESC,
                    (baseMessage.receiver as Group).description
                )
                messageIntent.putExtra(
                    Constants.CometChatConstant.GROUP_TYPE,
                    (baseMessage.receiver as Group).groupType
                )
                messageIntent.putExtra(
                    Constants.CometChatConstant.GROUP_OWNER,
                    (baseMessage.receiver as Group).owner
                )
                messageIntent.putExtra(
                    Constants.CometChatConstant.MEMBER_COUNT,
                    (baseMessage.receiver as Group).membersCount
                )
            }

            val pendingIntent:PendingIntent
            val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                pendingIntent = PendingIntent.getActivity(
                this.applicationContext,
                "0123".toInt(), messageIntent, pendingFlags
            )
//            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
//                // Add the intent, which inflates the back stack
//                addNextIntentWithParentStack(redirectionIntent)
//                // Get the PendingIntent containing the entire back stack
//                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//            }
//            val pendingIntent = stackBuilder.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.mipmap.ic_main_logo)
                .setContentTitle(json!!.getString("title"))
                .setContentText(json!!.getString("alert"))
//                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(this, R.color.azure_radiance))
                .setLargeIcon(getBitmapFromURL(baseMessage.sender.avatar))
                .setGroup(GROUP_ID)
                .setAutoCancel(true)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            if (baseMessage.type.equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitmapFromURL((baseMessage as MediaMessage).attachment.fileUrl))
                )
            }

            val summaryBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "2")
                .setContentTitle("Hipaasafe")
                .setContentText("$count messages")
                .setSmallIcon(R.mipmap.ic_main_logo)
                .setGroup(GROUP_ID)
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setColor(ContextCompat.getColor(this,R.color.dark_blue))
//                .setLargeIcon(getBitmapFromURL(baseMessage.sender.avatar))
                .setAutoCancel(true)
                .setGroupSummary(true)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            val notificationManager = NotificationManagerCompat.from(this)
            if (isCall) {
//                android.os.Debug.waitForDebugger()
//                Utils.showCallNotifcation(applicationContext, call!!)
                builder.setGroup(GROUP_ID + "Call")
                if (json!!.getString("alert") == "Incoming audio call" || json!!.getString("alert") == "Incoming video call") {
                    builder.setOngoing(true)
                    builder.priority = NotificationCompat.PRIORITY_HIGH
                    builder.setCategory(NotificationCompat.CATEGORY_CALL)
                    val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    val ringtone = RingtoneManager.getRingtone(applicationContext, alert)
                    val notification: Uri =
                        Uri.parse("android.resource://" + "com.hipaasafe" + "/" + R.raw.incoming_call)

                    builder.setSound(notification)
                    builder.addAction(
                        0,
                        "Answers",
                        PendingIntent.getBroadcast(
                            applicationContext,
                            REQUEST_CODE,
                            getCallIntent("Answers"),
                            pendingFlags
                        )
                    )
                    builder.addAction(
                        0,
                        "Decline",
                        PendingIntent.getBroadcast(
                            applicationContext,
                            1,
                            getCallIntent("Decline"),
                            pendingFlags
                        )
                    )
                } else {
                    notificationManager.cancel(5)
                    builder.setContentIntent(pendingIntent)
                }
                var isForeground: Boolean = false
                isForeground = BaseApplication.isForeground
                if (isForeground) {
                    Log.i("pranay", "isfor" + BaseApplication.isForeground)
                } else {
                    Log.i("pranay", "isBackground")
                    notificationManager.notify(5, builder.build())
                }
            } else {
                CometChat.markAsDelivered(baseMessage)
                builder.priority = NotificationCompat.PRIORITY_HIGH
                builder.setContentIntent(pendingIntent)
                builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                Uri notification = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.incoming_message);
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                notificationManager.notify(baseMessage.id, builder.build());
                notificationManager.notify(0, summaryBuilder.build());
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCallIntent(title: String): Intent {
        val callIntent = Intent(applicationContext, CallNotificationAction::class.java)
        callIntent.putExtra("SESSION_ID", call!!.sessionId)
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        callIntent.action = title
        return callIntent
    }
}