package com.hipaasafe.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.models.*
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

        fun setInstance() {
            if (INSTANCE == null) {
                INSTANCE = AppUtils()
            }
        }
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

    fun getUSIndex(countryList: ArrayList<CountryModel>): Int {
        for (i in 0 until countryList.size) {
            val country = countryList[i]
            if (country.dial_code.equals("+1")) {
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