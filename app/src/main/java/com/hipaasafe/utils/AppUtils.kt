package com.hipaasafe.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.media.AudioManager
import android.net.Uri
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CallSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.*
import com.google.android.material.snackbar.Snackbar
import com.hipaasafe.call_manager.CometChatCallActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hipaasafe.BuildConfig
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.presentation.login.model.CountryModel
import com.onesignal.OneSignal
import java.io.IOException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class AppUtils {

    lateinit var preferenceUtils: PreferenceUtils
    companion object {
        var INSTANCE: AppUtils? = null
        private const val TAG = "Utils"

        fun setInstance() {
            if (INSTANCE == null) {
                INSTANCE = AppUtils()
            }
        }
    }

    fun getDateId(var0: Long): String? {
        val var2 = Calendar.getInstance(Locale.ENGLISH)
        var2.timeInMillis = var0
        return DateFormat.format("ddMMyyyy", var2).toString()
    }

    fun getDate(var0: Long): String? {
        val var2 = Calendar.getInstance(Locale.ENGLISH)
        var2.timeInMillis = var0
        return DateFormat.format("dd/MM/yyyy", var2).toString()
    }
    fun getHeaderDate(timestamp: Long): String? {
        val messageTimestamp = Calendar.getInstance()
        messageTimestamp.timeInMillis = timestamp
        val now = Calendar.getInstance()
        //        if (now.get(5) == messageTimestamp.get(5)) {
        return DateFormat.format("hh:mm a", messageTimestamp).toString()
//        } else {
//            return now.get(5) - messageTimestamp.get(5) == 1 ? "Yesterday " + DateFormat.format("hh:mm a", messageTimestamp).toString() : DateFormat.format("d MMMM", messageTimestamp).toString() + " " + DateFormat.format("hh:mm a", messageTimestamp).toString();
//        }
    }
    fun openInMap(context: Context,latitude: Double,longitude: Double,label:String){
        val uriBegin = "geo:$latitude,$longitude"
        val encodedQuery = Uri.encode(label)
        val uriString = "$uriBegin?q=$encodedQuery&z=16"
        val uri = Uri.parse(uriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent)
    }

    fun getAddress(context: Context?, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.size > 0) {
                return addresses[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    val CHAT_USER_COLOR = intArrayOf(
        Color.parseColor("#F49F36"),
        Color.parseColor("#2196f3"),
        Color.parseColor("#9c27b0"),
        Color.parseColor("#673ab7"),
        Color.parseColor("#4caf50"),
        Color.parseColor("#ff5722"),
        Color.parseColor("#009688"),
        Color.parseColor("#607d8b"),
        Color.parseColor("#e91e63"),
        Color.parseColor("#D4F918"),
        Color.parseColor("#ff9800"),
        Color.parseColor("#795548"),
        Color.parseColor("#C5F7DF"),
        Color.parseColor("#9e9e9e"),
        Color.parseColor("#4C6800"),
        Color.parseColor("#A3B4A2"),
        Color.parseColor("#98F9E1"),
        Color.parseColor("#E60F0B"),
        Color.parseColor("#4BC2C2"),
        Color.parseColor("#2A3335"),
        Color.parseColor("#AD5BDF"),
        Color.parseColor("#02974C"),
        Color.parseColor("#F8FA64"),
        Color.parseColor("#29699A"),
        Color.parseColor("#2D2FFA"),
        Color.parseColor("#0FDED1")
    )
    fun joinOnGoingCall(context: Context) {
        val intent = Intent(context, CometChatCallActivity::class.java)
        intent.putExtra(Constants.CometChatConstant.JOIN_ONGOING, true)
        context.startActivity(intent)
    }

    fun getFileSize(fileSize: Int): String? {
        return if (fileSize > 1024) {
            if (fileSize > 1024 * 1024) {
                (fileSize / (1024 * 1024)).toString() + " MB"
            } else {
                (fileSize / 1024).toString() + " KB"
            }
        } else {
            "$fileSize B"
        }
    }

    fun getAudioManager(context: Context): AudioManager? {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    fun startCall(activity: Activity, call: Call, mainView: RelativeLayout?) {
        val callSettings = CallSettings.CallSettingsBuilder(activity, mainView)
            .setSessionId(call.sessionId)
            .startWithAudioMuted(true)
            .startWithVideoMuted(true)
            .build()
        CometChat.startCall(callSettings, object : CometChat.OngoingCallListener {
            override fun onUserJoined(user: User) {
                Log.e("onUserJoined: ", user.uid)
            }

            override fun onUserLeft(user: User) {
                Snackbar.make(
                    activity.window.decorView.rootView,
                    "User Left: " + user.name,
                    Snackbar.LENGTH_LONG
                ).show()
                Log.e("onUserLeft: ", user.uid)
            }

            override fun onError(e: CometChatException) {
                e.message?.let { Log.e("onError: ", it) }
                Toast.makeText(activity,e.code,Toast.LENGTH_SHORT).show()
            }

            override fun onCallEnded(call: Call) {
                Log.e(TAG, "onCallEnded: $call")
                activity.finish()
            }

            override fun onUserListUpdated(p0: MutableList<User>?) {
                Log.e(TAG, "onUserListUpdated: " + p0.toString())
            }

            override fun onAudioModesUpdated(p0: MutableList<AudioMode>?) {
                Log.e(TAG, "onAudioModesUpdated: " + p0.toString())
            }

        })
    }
    fun startCallIntent(
        context: Context, user: User, type: String?,
        isOutgoing: Boolean, sessionId: String
    ) {
        val videoCallIntent = Intent(context, CometChatCallActivity::class.java)
        videoCallIntent.putExtra(Constants.CometChatConstant.NAME, user.name)
        videoCallIntent.putExtra(Constants.CometChatConstant.UID, user.uid)
        videoCallIntent.putExtra(Constants.CometChatConstant.SESSION_ID, sessionId)
        videoCallIntent.putExtra(Constants.CometChatConstant.AVATAR, user.avatar)
        videoCallIntent.action = type
        videoCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (isOutgoing) {
            videoCallIntent.type = "outgoing"
        } else {
            videoCallIntent.type = "incoming"
        }
        context.startActivity(videoCallIntent)
    }

    fun startGroupCallIntent(
        context: Context, group: Group, type: String?,
        isOutgoing: Boolean, sessionId: String
    ) {
        val videoCallIntent = Intent(context, CometChatCallActivity::class.java)
        videoCallIntent.putExtra(Constants.CometChatConstant.NAME, group.name)
        videoCallIntent.putExtra(Constants.CometChatConstant.UID, group.guid)
        videoCallIntent.putExtra(Constants.CometChatConstant.SESSION_ID, sessionId)
        videoCallIntent.putExtra(Constants.CometChatConstant.AVATAR, group.icon)
        videoCallIntent.action = type
        videoCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (isOutgoing) {
            videoCallIntent.type = "outgoing"
        } else {
            videoCallIntent.type = "incoming"
        }
        context.startActivity(videoCallIntent)
    }

    fun getLastMessageDate(timestamp: Long): String? {
        val lastMessageTime = SimpleDateFormat("h:mm a").format(Date(timestamp * 1000))
        val lastMessageDate = SimpleDateFormat("dd/MM/yyyy").format(Date(timestamp * 1000))
        val lastMessageWeek = SimpleDateFormat("EEE").format(Date(timestamp * 1000))
        val currentTimeStamp = System.currentTimeMillis()
        val diffTimeStamp = currentTimeStamp - timestamp * 1000
        Log.e("TEST", "getLastMessageDate: " + 24 * 60 * 60 * 1000)
        return if (diffTimeStamp < 24 * 60 * 60 * 1000) {
            lastMessageTime
        } else if (diffTimeStamp < 48 * 60 * 60 * 1000) {
            "Yesterday"
        } else if (diffTimeStamp < 7 * 24 * 60 * 60 * 1000) {
            lastMessageWeek
        } else {
            lastMessageDate
        }
    }
    fun isLoggedInUser(user: User): Boolean {
        return user.uid == CometChat.getLoggedInUser().uid
    }

    fun getLastMessage(context: Context, lastMessage: BaseMessage): String? {
        var message: String? = null
        if (lastMessage.deletedAt == 0L) {
            when (lastMessage.category) {
                CometChatConstants.CATEGORY_MESSAGE ->
                    if (lastMessage is TextMessage) {
                        message = if (isLoggedInUser(lastMessage.getSender()))
                            context.getString(R.string.you) + ": " + if (lastMessage.text == null) {
                                if (isLoggedInUser(
                                        lastMessage.sender
                                    )
                                ) context.getString(R.string.you_deleted_this_message) else context.getString(
                                    R.string.this_message_deleted
                                )
                            } else lastMessage.text
                        else
                            lastMessage.getSender().name + ": " + lastMessage.text

                    } else if (lastMessage is MediaMessage) {
                        if (lastMessage.getDeletedAt() == 0L) {
                            if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_IMAGE) message =
                                context.getString(
                                    R.string.message_image
                                ) else if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_VIDEO) message =
                                context.getString(
                                    R.string.message_video
                                ) else if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_FILE) message =
                                context.getString(
                                    R.string.message_file
                                ) else if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_AUDIO) message =
                                context.getString(
                                    R.string.message_audio
                                )
                        } else message =
                            if (isLoggedInUser(lastMessage.sender)) context.getString(R.string.you_deleted_this_message) else context.getString(
                                R.string.this_message_deleted
                            )
                    }
                CometChatConstants.CATEGORY_CUSTOM ->
                    message = if (lastMessage.deletedAt == 0L) {
                        when {
                            lastMessage.type == Constants.CometChatConstant.LOCATION -> context.getString(
                                R.string.custom_message_location
                            )
                            lastMessage.type.equals(
                                Constants.CometChatConstant.STICKERS, ignoreCase = true
                            ) -> context.getString(
                                R.string.custom_message_sticker
                            )
                            lastMessage.type.equals(
                                Constants.CometChatConstant.MEETING, ignoreCase = true
                            ) -> context.getString(
                                R.string.custom_message_meeting
                            )
                            else -> String.format(
                                context.getString(
                                    R.string.you_received
                                ), lastMessage.type
                            )
                        }
                    } else if (isLoggedInUser(lastMessage.sender)) context.getString(R.string.you_deleted_this_message) else context.getString(
                        R.string.this_message_deleted
                    )
//                    CometChatConstants.CATEGORY_ACTION -> message = (lastMessage as Action).message
                CometChatConstants.CATEGORY_ACTION -> if (lastMessage is Action) {
                    when (lastMessage.action) {
                        CometChatConstants.ActionKeys.ACTION_JOINED -> message =
                            (lastMessage.actioBy as User).name + " " + context.getString(R.string.joined)
                        CometChatConstants.ActionKeys.ACTION_MEMBER_ADDED -> message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.added) + " " + (lastMessage.actionOn as User).name)
                        CometChatConstants.ActionKeys.ACTION_KICKED -> message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.removed) + " " + (lastMessage.actionOn as User).name)
                        CometChatConstants.ActionKeys.ACTION_BANNED -> message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.blocked) + " " + (lastMessage.actionOn as User).name)
                        CometChatConstants.ActionKeys.ACTION_UNBANNED -> message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.unblocked) + " " + (lastMessage.actionOn as User).name)
                        CometChatConstants.ActionKeys.ACTION_LEFT -> message =
                            (lastMessage.actioBy as User).name + " " + context.getString(
                                R.string.left
                            )
                        CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED -> message = if (lastMessage.newScope == CometChatConstants.SCOPE_MODERATOR) {
                            ((lastMessage.actioBy as User).name + " " + context.getString(R.string.made) + " "
                                    + (lastMessage.actionOn as User).name + " " + context.getString(
                                R.string.moderator
                            ))
                        } else if (lastMessage.newScope == CometChatConstants.SCOPE_ADMIN) {
                            ((lastMessage.actioBy as User).name + " " + context.getString(R.string.made) + " "
                                    + (lastMessage.actionOn as User).name + " " + context.getString(
                                R.string.admin
                            ))
                        } else if (lastMessage.newScope == CometChatConstants.SCOPE_PARTICIPANT) {
                            ((lastMessage.actioBy as User).name + " " + context.getString(R.string.made) + " "
                                    + (lastMessage.actionOn as User).name + " " + context.getString(
                                R.string.participant
                            ))
                        } else lastMessage.message
                    }
                }
                CometChatConstants.CATEGORY_CALL ->
                    message = if ((lastMessage as Call).callStatus.equals(
                            CometChatConstants.CALL_STATUS_ENDED,
                            ignoreCase = true
                        ) ||
                        lastMessage.callStatus.equals(
                            CometChatConstants.CALL_STATUS_CANCELLED,
                            ignoreCase = true
                        )
                    ) {
                        if (lastMessage.getType()
                                .equals(CometChatConstants.CALL_TYPE_AUDIO, ignoreCase = true)
                        ) context.getString(
                            R.string.incoming_audio_call
                        ) else context.getString(
                            R.string.incoming_video_call
                        )
                    } else if (lastMessage.callStatus.equals(
                            CometChatConstants.CALL_STATUS_ONGOING,
                            ignoreCase = true
                        )
                    ) {
                        context.getString(R.string.ongoing_call)
                    } else if (lastMessage.callStatus.equals(
                            CometChatConstants.CALL_STATUS_CANCELLED,
                            ignoreCase = true
                        ) ||
                        lastMessage.callStatus.equals(
                            CometChatConstants.CALL_STATUS_UNANSWERED,
                            ignoreCase = true
                        ) ||
                        lastMessage.callStatus.equals(
                            CometChatConstants.CALL_STATUS_BUSY,
                            ignoreCase = true
                        )
                    ) {
                        if (lastMessage.getType()
                                .equals(CometChatConstants.CALL_TYPE_AUDIO, ignoreCase = true)
                        ) context.getString(
                            R.string.missed_voice_call
                        ) else context.getString(
                            R.string.missed_video_call
                        )
                    } else lastMessage.callStatus + " " + lastMessage.getType() + " Call"
                else -> message =
                    context.getString(R.string.tap_to_start_conversation)
            }
            return message
        } else return if (isLoggedInUser(lastMessage.sender)) context.getString(R.string.you_deleted_this_message) else context.getString(
            R.string.this_message_deleted
        )
    }

    fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd MMM yyyy")
        return sdf.format(time)
    }

    fun convertSecondToTime(millis: Long): String {
        val secs: Long = millis / 1000
        return String.format("%02d:%02d", (secs % 3600) / 60, secs % 60);

    }

    fun isValidEmail(email: String?) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidMobileNumber(s: String): Boolean {
        return if (s.isNotEmpty()) {
            val p = Pattern.compile("(0/91)?[6-9][0-9]{9}")
            val m = p.matcher(s)
            m.find() && m.group() == s
        } else {
            true
        }
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {

        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }
        return jsonString
    }

    fun getPlayerId(): String? {
        var playerID: String? = ""

        val deviceState = OneSignal.getDeviceState()
        playerID = deviceState?.userId
        return playerID
    }

    fun openDialer(context: Context, infoMobile: String) {
        val i = Intent(Intent.ACTION_DIAL)
        i.data = Uri.parse("tel:" + infoMobile)
        context.startActivity(i)
    }

    fun openMailer(context: Context, mailTo: String) {
        val i = Intent(Intent.ACTION_SENDTO)
        i.data = Uri.parse("mailto: " + mailTo)
        context.startActivity(i)

    }

    fun getSpecialityWithLocationAndExperience(
        speciality: String?,
        location: String?,
        experience: String?
    ): String {
        var result = ""
        if (!TextUtils.isEmpty(speciality)) {
            result = "$speciality - "
        }
        if (!TextUtils.isEmpty(location)) {
            result = "$result$location - "
        }
        if (!TextUtils.isEmpty(experience) || !TextUtils.equals(experience, "0.0")) {
            result = "$result$experience yrs"
        }
        return result
    }

    fun getCountriesList(context: Context): ArrayList<CountryModel> {
        val countryCodeListString =
            INSTANCE?.getJsonDataFromAsset(context, "countries.json")
        val countryType: Type? = object : TypeToken<ArrayList<CountryModel?>>() {}.type
        return INSTANCE?.getDataFromJson(
            countryCodeListString ?: "",
            countryType
        ) as ArrayList<CountryModel>
    }
    fun getDataFromJson(stringData: String, objectType: Type?): Any {
        val gson = Gson()
        return gson.fromJson(stringData, objectType)
    }

    fun getCountryIndex(countryCode:String,countryList: ArrayList<CountryModel>): Int {
        for (i in 0 until countryList.size) {
            val country = countryList[i]
            if (country.dial_code.equals(countryCode)) {
                return i
            }
        }
        return 0
    }

    fun logMe(tag: String, message: String?) {
        if(BuildConfig.DEBUG){
            Log.e(tag, message ?: "")
        }
    }

    fun showView(view: View, duration: Long) {
        //Alpha 1 indicates completely visible
        view.animate().alpha(1.0f).duration = 400

        //        view.visibility = View.VISIBLE
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

    fun hideView(view: View, duration: Long) {
        //alpha 0 indicates completely transaparent
        //        view.visibility = View.GONE
        view.animate().alpha(0f).duration = 400
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

    fun showFadeView(view: View, duration: Long) {
        //        view.visibility = View.VISIBLE

        view.animate().alpha(1.0f).duration = duration
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

    fun hideFadeView(view: View, duration: Long) {
        //        view.visibility = View.GONE

        view.animate().alpha(0.2f).duration = duration
        //            .setListener(object : AnimatorListenerAdapter() {
        //                override fun onAnimationEnd(animation: Animator) {
        //                    view.setVisibility(View.GONE)
        //                }
        //            })
    }

}