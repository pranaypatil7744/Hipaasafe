package com.hipaasafe.call_manager

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CallSettings
import com.cometchat.pro.core.CallSettings.CallSettingsBuilder
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.AudioMode
import com.cometchat.pro.models.User
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity

class CometChatStartCallActivity : BaseActivity() {


    lateinit var activity: CometChatStartCallActivity

    private lateinit var mainView: RelativeLayout

    private var sessionID: String? = null

    private lateinit var callSettings: CallSettings

    private var callType: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cometchat_start_call)
        activity = this
        mainView = findViewById(R.id.call_view)
        if (intent != null) {
            sessionID = intent.getStringExtra(Constants.CometChatConstant.SESSION_ID)
            callType = intent.getStringExtra(Constants.CometChatConstant.TYPE)
        }

        val builder = CallSettingsBuilder(this, mainView)
            .setSessionId(sessionID)
        if(callType == "audio") {
            builder.startWithVideoMuted(true)
        } else {
            builder.startWithVideoMuted(false)
        }

        callSettings = builder.build()

        CometChat.startCall(callSettings, object : CometChat.OngoingCallListener {
            override fun onUserJoined(p0: User?) {
                p0?.uid?.let { Log.e("onUserJoined: ", it) }
            }

            override fun onUserLeft(p0: User?) {
                p0?.uid?.let { Log.e("onUserLeft: ", it) }
            }

            override fun onError(p0: CometChatException) {
                p0.message?.let { Log.e("onstartcallError: ", it) }
                showToast(p0.code)
            }

            override fun onCallEnded(p0: Call?) {
                Log.e("TAG", "onCallEnded: ")
                showToast()
                finish()
            }

            override fun onUserListUpdated(p0: MutableList<User>?) {
                Log.e("TAG", "onUserListUpdated: " + p0.toString())
            }

            override fun onAudioModesUpdated(p0: MutableList<AudioMode>?) {
                Log.e("TAG", "onAudioModesUpdated: "+p0.toString() )
            }

        })
    }

    private fun showToast() {
        val layoutInflater = layoutInflater
        val layout :View = layoutInflater.inflate(R.layout.custom_toast, findViewById<ViewGroup>(R.id.group_layout))
        val tvMessage = layout.findViewById<TextView>(R.id.message)
        tvMessage.text = "Call Ended"
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,0)
        toast.view = layout

        toast.show()
    }
}