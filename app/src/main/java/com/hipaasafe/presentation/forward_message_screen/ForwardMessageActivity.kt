package com.hipaasafe.presentation.forward_message_screen

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.ConversationsRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.*
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityForwardMessageBinding
import com.hipaasafe.presentation.home_screen.comet_chat_group.adapter.GroupChatListAdapter
import com.hipaasafe.utils.MediaUtilsCometChat
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ForwardMessageActivity : BaseActivity(), GroupChatListAdapter.ChatListClickManager {
    lateinit var binding: ActivityForwardMessageBinding
    var conversationList: ArrayList<Conversation>? = ArrayList()
    var originalConversationList: ArrayList<Conversation> = ArrayList()
    lateinit var chatListAdapter: GroupChatListAdapter
    private var conversationsRequest: ConversationsRequest? = null
    private var selectedList: ArrayList<String> = ArrayList()
    private var messageCategory: String? = null
    private var messageType: String? = null
    private var textMessage = ""
    private var uri: Uri? = null
    private var lat = 0.0
    private var lon = 0.0
    private var mediaMessageUrl: String? = null
    private var mediaMessageExtension: String? = null
    private var mediaMessageName: String? = null
    private var mediaMessageMime: String? = null
    private var mediaMessageSize = 0
    private var id: String = ""
    var isSearch: Boolean = false
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForwardMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpView()
        getIntentData()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            btnForwardMsg.setOnClickListener {
                when {
                    selectedList.size == 0 -> {
                        showToast(getString(R.string.please_select_at_least_one_conversation))
                    }
                    selectedList.size > 5 -> {
                        showToast(getString(R.string.you_can_t_select_more_than_5_conversions))
                    }
                    else -> {
                        sendMessageToAll()
                    }
                }
            }
            etSearchDoctor.doOnTextChanged { text, start, before, count ->
                if (originalConversationList.isNotEmpty()) {
                    if (text.toString().isNotEmpty()) {
                        isSearch = true
                        searchDoctor(text.toString().trim().toLowerCase(Locale.ROOT))
                    } else {
                        isSearch = false
                        conversationList?.clear()
                        conversationList?.addAll(originalConversationList)
                        chatListAdapter.notifyDataSetChanged()
                        checkNoConversation()
                    }
                }
            }
            imgClose.setOnClickListener {
                etSearchDoctor.setText("")
                isSearch = false
                conversationList?.clear()
                if (originalConversationList.isNotEmpty()) {
                    conversationList?.addAll(originalConversationList)
                    chatListAdapter.notifyDataSetChanged()
                    checkNoConversation()
                }
            }
        }
    }

    private fun searchDoctor(searchText: String) {
        binding.apply {
            val searchList = originalConversationList.filter {
                if (it.conversationType == CometChatConstants.RECEIVER_TYPE_USER) {
                    (it.conversationWith as User).name.trim().toLowerCase(Locale.ROOT)
                        .contains(searchText)
                } else {
                    (it.conversationWith as Group).name.trim().toLowerCase(Locale.ROOT)
                        .contains(searchText)
                }
            }

            if (searchList.isNotEmpty()) {
                conversationList?.clear()
                conversationList?.addAll(searchList)
                chatListAdapter.notifyDataSetChanged()
            } else {
                conversationList?.clear()
                chatListAdapter.notifyDataSetChanged()
            }
            checkNoConversation()
        }
    }

    private fun checkNoConversation() {
        binding.apply {
            if (conversationList?.size == 0) {
                if (isSearch) {
                    tvNoChatMsg.text = getString(R.string.no_matching_data_found)
                } else {
                    tvNoChatMsg.text = getString(R.string.no_chats_found_to_forward)

                }
                layoutNoChat.visibility = VISIBLE
                recyclerMembers.visibility = GONE
            } else {
                layoutNoChat.visibility = GONE
                recyclerMembers.visibility = VISIBLE
            }
        }
    }

    private fun sendMessageToAll() {
        binding.apply {

            val selectedConversation: ArrayList<Conversation> = ArrayList()
            selectedConversation.clear()
            for (i in 0 until selectedList.size) {
                val list = conversationList?.find {
                    it.conversationId == selectedList[i]
                }
                if (list != null) {
                    selectedConversation.add(list)
                }
            }
            progressDialog =
                ProgressDialog.show(this@ForwardMessageActivity, "", "Sending Message..")

            if (messageCategory == CometChatConstants.CATEGORY_MESSAGE) {
                if (messageType != null && messageType == CometChatConstants.MESSAGE_TYPE_TEXT) {
                    Thread(Runnable {
                        for (i in 0 until selectedConversation.size) {
                            val conversation = selectedConversation[i]
                            var message: TextMessage
                            var uid: String
                            var type: String
                            if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
                                uid = (conversation.conversationWith as User).uid
                                type = CometChatConstants.RECEIVER_TYPE_USER
                            } else {
                                uid = (conversation.conversationWith as Group).guid
                                type = CometChatConstants.RECEIVER_TYPE_GROUP
                            }
                            val jsonObject = JSONObject()
                            jsonObject.put(Constants.IsForwarded, true)
                            message = TextMessage(uid, textMessage, type)
                            message.metadata = jsonObject
                            sendMessage(message)
                            if (i == selectedConversation.size - 1) {
                                finish()
                            }
                        }
                    }).start()
                } else if (messageType != null && messageType == CometChatConstants.MESSAGE_TYPE_IMAGE ||
                    messageType == CometChatConstants.MESSAGE_TYPE_AUDIO ||
                    messageType == CometChatConstants.MESSAGE_TYPE_VIDEO ||
                    messageType == CometChatConstants.MESSAGE_TYPE_FILE
                ) {
                    Thread(Runnable {
                        for (i in 0 until selectedConversation.size) {
                            val conversation = selectedConversation[i]
                            var message: MediaMessage
                            var uid: String
                            var type: String
                            if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
                                uid = (conversation.conversationWith as User).uid
                                type = CometChatConstants.RECEIVER_TYPE_USER
                            } else {
                                uid = (conversation.conversationWith as Group).guid
                                type = CometChatConstants.RECEIVER_TYPE_GROUP
                            }
                            message = MediaMessage(uid, null, messageType, type)
                            val attachment = Attachment()
                            attachment.fileUrl = mediaMessageUrl
                            attachment.fileMimeType = mediaMessageMime
                            attachment.fileSize = mediaMessageSize
                            attachment.fileExtension = mediaMessageExtension
                            attachment.fileName = mediaMessageName
                            message.attachment = attachment
                            val jsonObject = JSONObject()
                            jsonObject.put(Constants.IsForwarded, true)
                            message.metadata = jsonObject
                            sendMediaMessage(message)
                            if (i == selectedConversation.size - 1) {
                                Handler(mainLooper).postDelayed({
                                    finish()
                                }, 2000)
                            }
                        }
                    }).start()
                } else {
                    Thread {
                        for (i in 0 until selectedConversation.size) {
                            val conversation = selectedConversation[i]
                            var message: MediaMessage? = null
                            var uid: String
                            var type: String
                            if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
                                uid = (conversation.conversationWith as User).uid
                                type = CometChatConstants.RECEIVER_TYPE_USER
                            } else {
                                uid = (conversation.conversationWith as Group).guid
                                type = CometChatConstants.RECEIVER_TYPE_GROUP
                            }
                            val file = uri?.let { uri ->
                                MediaUtilsCometChat.saveDriveFile(
                                    this@ForwardMessageActivity,
                                    uri
                                )
                            }
                            if (file != null && messageType == CometChatConstants.MESSAGE_TYPE_IMAGE)
                                message = MediaMessage(uid, file, messageType, type)
                            else if (file != null && messageType == CometChatConstants.MESSAGE_TYPE_VIDEO)
                                message = MediaMessage(uid, file, messageType, type)
                            else if (file != null && messageType == CometChatConstants.MESSAGE_TYPE_AUDIO)
                                message = MediaMessage(uid, file, messageType, type)
                            else if (file != null && messageType == CometChatConstants.MESSAGE_TYPE_FILE)
                                message = MediaMessage(uid, file, messageType, type)

                            val jsonObject = JSONObject()
                            jsonObject.put(Constants.IsForwarded, true)
                            message?.metadata = jsonObject
                            message?.let { it1 -> sendMediaMessage(it1) }
                            if (i == selectedConversation.size - 1) {
                                finish()
                            }
                        }
                    }.start()
                }
            } else {
                if (messageType != null && messageType == Constants.CometChatConstant.LOCATION
                ) {
                    Thread {
                        for (i in 0 until selectedConversation.size) {
                            val conversation = selectedConversation[i]
                            var message: CustomMessage
                            var uid: String
                            var customData = JSONObject()
                            var type: String
                            if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
                                uid = (conversation.conversationWith as User).uid
                                type = CometChatConstants.RECEIVER_TYPE_USER
                            } else {
                                uid = (conversation.conversationWith as Group).guid
                                type = CometChatConstants.RECEIVER_TYPE_GROUP
                            }
                            try {
                                customData = JSONObject()
                                customData.put(Constants.CometChatConstant.LOCATION_LATITUDE, lat)
                                customData.put(Constants.CometChatConstant.LOCATION_LONGITUDE, lon)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            message = CustomMessage(
                                uid,
                                type,
                                Constants.CometChatConstant.LOCATION,
                                customData
                            )
                            val jsonObject = JSONObject()
                            jsonObject.put(Constants.IsForwarded, true)
                            message.metadata = jsonObject
                            sendLocationMessage(message)
                            if (i == selectedConversation.size - 1) {
                                finish()
                            }
                        }
                    }.start()
                }
            }
        }
    }

    private fun sendLocationMessage(message: CustomMessage) {
        CometChat.sendCustomMessage(message, object : CometChat.CallbackListener<CustomMessage>() {
            override fun onSuccess(customMessage: CustomMessage) {
                Log.e(TAG, "onSuccess: " + customMessage.receiverUid)
            }

            override fun onError(e: CometChatException) {
                Log.e(TAG, "onErrorCustom: " + e.message)
            }
        })
    }

    private fun sendMessage(message: TextMessage) {
        CometChat.sendMessage(message, object : CometChat.CallbackListener<TextMessage>() {
            override fun onSuccess(textMessage: TextMessage) {
                Log.e(TAG, "onSuccess: " + textMessage.receiverUid)
            }

            override fun onError(e: CometChatException) {
                Log.e(TAG, "onError: " + e.message)
            }
        })
    }

    private fun sendMediaMessage(mediaMessage: MediaMessage) {
        CometChat.sendMediaMessage(
            mediaMessage,
            object : CometChat.CallbackListener<MediaMessage>() {
                override fun onSuccess(mediaMessage: MediaMessage) {
                    try {
                        if ((progressDialog != null) && progressDialog?.isShowing == true) {
                            progressDialog?.dismiss();
                        }
                    } catch (e: IllegalArgumentException) {
                    } catch (e: Exception) {
                    } finally {
                        progressDialog = null
                    }
                    finish()
                    Log.d(TAG, "sendMediaMessage onSuccess: $mediaMessage")
                }

                override fun onError(e: CometChatException) {
                    Log.e(TAG, "onError: " + e.message)
                }
            })
    }

    private fun setUpView() {
        binding.apply {
            recyclerMembers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        if (!isSearch) {
                            makeConversationList()
                        }
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        makeConversationList()
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun makeConversationList() {
        if (conversationsRequest == null) {
            conversationsRequest =
                ConversationsRequest.ConversationsRequestBuilder().setLimit(30).build()
        }
        toggleLoader(true)
        conversationsRequest?.fetchNext(object : CometChat.CallbackListener<List<Conversation>>() {
            override fun onSuccess(list: List<Conversation>) {
                toggleLoader(false)
                if (list.isNotEmpty()) {
                    setUpAdapter(list)
                } else {
                    checkNoConversation()
                }
            }

            override fun onError(e: CometChatException) {
                toggleLoader(false)
                checkNoConversation()
                showToast(e.message.toString())
            }
        })
    }

    private fun setUpAdapter(conversations: List<Conversation>) {
        binding.apply {
            if (::chatListAdapter.isInitialized) {
                for (i in conversations) {
                    if (!i.conversationId.contains(id)) {
                        conversationList?.add(i)
                    }
                }
                originalConversationList.clear()
                originalConversationList.addAll(conversationList ?: arrayListOf())
                chatListAdapter.notifyDataSetChanged()
            } else {
                conversationList?.clear()
                selectedList.clear()
                for (i in conversations) {
                    if (!i.conversationId.contains(id)) {
                        conversationList?.add(i)
                    }
                }
                chatListAdapter = GroupChatListAdapter(
                    this@ForwardMessageActivity,
                    conversationList ?: arrayListOf(),
                    this@ForwardMessageActivity,
                    isForwardMsg = true,
                    selectedList = selectedList
                )
                originalConversationList.clear()
                originalConversationList.addAll(conversationList ?: arrayListOf())
                recyclerMembers.adapter = chatListAdapter
            }
        }
    }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                id = getString(Constants.CometChatConstant.UID).toString()
                messageCategory = getString(Constants.CometChatConstant.MESSAGE_CATEGORY)
                messageType = getString(Constants.CometChatConstant.TYPE)
                when (messageCategory) {
                    CometChatConstants.CATEGORY_MESSAGE -> {
                        if (messageType == CometChatConstants.MESSAGE_TYPE_TEXT) {
                            textMessage = getString(CometChatConstants.MESSAGE_TYPE_TEXT).toString()
                        } else if (messageType == CometChatConstants.MESSAGE_TYPE_IMAGE ||
                            messageType == CometChatConstants.MESSAGE_TYPE_AUDIO ||
                            messageType == CometChatConstants.MESSAGE_TYPE_VIDEO ||
                            messageType == CometChatConstants.MESSAGE_TYPE_FILE
                        ) {
                            mediaMessageName = getString(Constants.CometChatConstant.FILE_NAME)
                            mediaMessageUrl = getString(Constants.CometChatConstant.FILE_URL)
                            mediaMessageMime = getString(Constants.CometChatConstant.FILE_MIME_TYPE)
                            mediaMessageExtension =
                                getString(Constants.CometChatConstant.FILE_EXTENSION)
                            mediaMessageSize = getInt(Constants.CometChatConstant.FILE_SIZE)
                        }
                    }
                    CometChatConstants.CATEGORY_CUSTOM -> {
                        if (messageType == Constants.CometChatConstant.LOCATION) {
                            lat = getDouble(Constants.CometChatConstant.LOCATION_LATITUDE)
                            lon = getDouble(Constants.CometChatConstant.LOCATION_LONGITUDE)
                        }
                    }
                }
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.forward_message)
            tvDate.visibility = GONE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onSelectChat(position: Int, conversationId: String, name: String) {
        if (selectedList.size >= 5) {
            showToast(getString(R.string.you_can_t_select_more_than_5_conversions))
        } else {
            selectedList.add(conversationId)
            chatListAdapter.notifyDataSetChanged()
        }
    }

    override fun onRemoveChat(position: Int, conversationId: String, name: String) {
        selectedList.remove(conversationId)
        chatListAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "CometChatForward"
    }
}