package com.hipaasafe.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User
import com.google.android.material.snackbar.Snackbar
import com.hipaasafe.AppConfig
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.presentation.home_screen.HomeActivity
import org.json.JSONObject

class CometChatUtils {
    companion object{
        private const val TAG = "CometChatUtils"

        fun startCallIntent(
            context: Context, user: User, type: String?,
            isOutgoing: Boolean, sessionId: String
        ) {
            //todo handle call screen
            val videoCallIntent = Intent(context, HomeActivity::class.java)
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
            //todo handle call screen
            val videoCallIntent = Intent(context, HomeActivity::class.java)
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

        fun logoutFromComet() {
            CometChat.logout(object : CometChat.CallbackListener<String?>() {
                override fun onSuccess(p0: String?) {
                    AppUtils.INSTANCE?.logMe(TagName.COMET_SDK, "Logout from comet successfull")

                }

                override fun onError(e: CometChatException?) {
                    AppUtils.INSTANCE?.logMe(
                        TagName.COMET_SDK,
                        "Logout from comet failure : " + e?.localizedMessage
                    )
                }
            })
        }

        fun loginToComet(userId: String?, name: String?,listener:CometListener?,token:String) {
//            CometChat.login(userId?:"",AppConfig.AppDetails.AUTH_KEY,object:CometChat.CallbackListener<User?>(){
            /****
             * shailesh - sudhir
             * vijay
             * nakash
             * jigesh - suleman
             */
            CometChat.login(
                userId ?: "",
                AppConfig.AppDetails.AUTH_KEY,
                object : CometChat.CallbackListener<User?>() {
                    override fun onSuccess(p0: User?) {
                        AppUtils.INSTANCE?.logMe(TagName.COMET_SDK, "Login from comet successfull")
                        listener?.onCometLoginSuccess()
                        CometChat.registerTokenForPushNotification(token, object : CometChat.CallbackListener<String?>() {
                            override fun onSuccess(s: String?) {
                                Log.e("onSuccessPN: ", s.toString())
                            }

                            override fun onError(e: CometChatException) {
                                Log.e("onErrorPN: ", e.message.toString())
                            }
                        })
                    }

                    override fun onError(e: CometChatException?) {
                        listener?.onCometLoginFailure()
                        AppUtils.INSTANCE?.logMe(
                            TagName.COMET_SDK,
                            "Login from comet failure : " + e?.localizedMessage
                        )
                        when (e?.code) {
                            "ERR_UID_NOT_FOUND" -> {
                                /*** User is not present in db,
                                 * This scenario will not come as user is registered into comet from backend, once registration is done.
                                 * ****/
//                            registerToComet(userId,name)
                            }
                        }
                    }
                })
        }


        fun initiatecall(
            context: Context,
            recieverID: String?,
            receiverType: String?,
            callType: String?
        ) {
            val call = Call(recieverID!!, receiverType, callType)
            val jsonObject = JSONObject()
            try {
                jsonObject.put("bookingId", 6)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            call.metadata = jsonObject
            CometChat.initiateCall(call, object : CometChat.CallbackListener<Call>() {
                override fun onSuccess(call: Call) {
                    if (receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                        startGroupCallIntent(
                            context,
                            call.callReceiver as Group,
                            call.type,
                            true,
                            call.sessionId
                        )
                    } else {
                        startCallIntent(
                            context,
                            call.callReceiver as User,
                            call.type,
                            true,
                            call.sessionId
                        )
                    }
                }

                override fun onError(e: CometChatException) {
                    Log.e(TAG, "onError: " + e.message)
                    Snackbar.make(
                        (context as Activity).window.decorView.rootView,
                        context.getResources()
                            .getString(R.string.call_initiate_error) + ":" + e.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}

interface CometListener{
    fun onCometLoginSuccess()
    fun onCometLoginFailure()
}