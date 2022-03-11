package com.hipaasafe.presentation.home_screen.comet_chat_group.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.MessagesRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.helpers.CometChatHelper
import com.cometchat.pro.models.*
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemGroupChatListBinding
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType

class GroupChatListAdapter(
    val context: Context,
    private val conversationList: ArrayList<Conversation>,
    private val listener: ChatListClickManager,
    private val isForwardMsg: Boolean = false,
    private var selectedList: ArrayList<String>? = ArrayList()

) : RecyclerView.Adapter<GroupChatListAdapter.ViewHolder>() {

    private var deliveryReceiptsEnabled: Boolean = false

    class ViewHolder(val binding: ItemGroupChatListBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        getFeatureStatus()
    }

    private fun getFeatureStatus() {
        CometChat.isFeatureEnabled(
            Constants.CometChatConstant.chat_messages_receipts_enabled,
            object : CometChat.CallbackListener<Boolean>() {
                override fun onSuccess(p0: Boolean?) {
                    if (p0 != null) {
                        deliveryReceiptsEnabled = p0
                    }
                }

                override fun onError(p0: CometChatException?) {
                    Log.e("FeatureRestriction", "onError: isUserPresenceEnabled " + p0.toString())
                }
            })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_group_chat_list, parent, false)
        val binding = ItemGroupChatListBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = conversationList[position]
        val baseMsg = data.lastMessage
        var name: String = ""
        val icon: String?

        if (data.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
            val userDetails = data.conversationWith as User?
            name = userDetails?.name.toString()
            icon = userDetails?.avatar
        } else {
            val groupDetails = data.conversationWith as Group?
            val loginUser = PreferenceUtils(context).getValue(Constants.PreferenceKeys.role_id).toIntOrNull()
            name = if (loginUser == LoginUserType.PATIENT.value){
                groupDetails?.name.toString().split("|").first()
            }else{
                groupDetails?.name.toString().split("|").last()
            }
            icon = groupDetails?.icon
        }
        holder.binding.apply {
            tvName.text = name
            tvCount.text = data.unreadMessageCount.toString()
            if (baseMsg != null) {
                tvDate.text = AppUtils.INSTANCE?.getLastMessageDate(baseMsg.sentAt)
                tvLastMessage.text = AppUtils.INSTANCE?.getLastMessage(context, baseMsg)
                if (data.unreadMessageCount > 0 && !isForwardMsg) {
                    tvCount.visibility = VISIBLE
                    callLastReadMsgApi(
                        id = if (data.conversationType == CometChatConstants.RECEIVER_TYPE_USER) baseMsg.sender.uid else baseMsg.receiverUid,
                        isGroup = data.conversationType == CometChatConstants.RECEIVER_TYPE_GROUP,
                        msgLimit = data.unreadMessageCount,
                        view = holder.binding
                    )
                } else {
                    tvCount.visibility = GONE
                    imgImpMsg.visibility = GONE
                    tvLastMessage.setTextColor(ContextCompat.getColor(context, R.color.gray))
                }
                if (data.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
                    ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgProfile, icon)
//                    if ((data.conversationWith as User?)?.status == CometChatConstants.USER_STATUS_ONLINE){
//                        imgOnlineStatus.visibility = VISIBLE
//                    }else{
//                        imgOnlineStatus.visibility = INVISIBLE
//                    }
                    if (deliveryReceiptsEnabled && !isForwardMsg) {
                        if (baseMsg.receiverType == CometChatConstants.RECEIVER_TYPE_USER && baseMsg.sender.uid == CometChat.getLoggedInUser().uid) {
                            imgMsgStatus.visibility = VISIBLE
                            when {
                                baseMsg.readAt != 0L -> {
                                    imgMsgStatus.setImageResource(R.drawable.ic_read_msg)
                                }
                                baseMsg.deliveredAt != 0L -> {
                                    imgMsgStatus.setImageResource(R.drawable.ic_deliver_msg)
                                }
                                else -> {
                                    imgMsgStatus.setImageResource(R.drawable.ic_send_msg)
                                }
                            }
                        } else {
                            imgMsgStatus.visibility = GONE
                        }
                    } else {
                        imgMsgStatus.visibility = GONE
                    }
                } else {
                    ImageUtils.INSTANCE?.loadRemoteImageForGroupProfile(imgProfile, icon)
                    imgMsgStatus.visibility = GONE
                }
            }

        }
        holder.itemView.setOnClickListener {
            if (data.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
                listener.clickOnPersonalChat(position, data.conversationWith as User)
            } else {
                listener.clickOnChatGroup(position, data.conversationWith as Group)
            }
        }

        // for forward msg

        holder.binding.apply {
            if (isForwardMsg) {
                btnIsSelect.visibility = VISIBLE
                space.visibility = VISIBLE
            } else {
                btnIsSelect.visibility = GONE
                space.visibility = GONE
            }

            if (selectedList?.isNotEmpty() == true) {
                val checkIsSelected = selectedList?.find {
                    it == data.conversationId
                }
                if (checkIsSelected == null) {
                    holder.binding.btnIsSelect.setImageResource(R.drawable.ic_item_not_selected)
                } else {
                    holder.binding.btnIsSelect.setImageResource(R.drawable.ic_item_selected)
                }
            } else {
                holder.binding.btnIsSelect.setImageResource(R.drawable.ic_item_not_selected)
            }

            btnIsSelect.setOnClickListener {
                if (selectedList?.isNotEmpty() == true) {
                    val checkIsSelected = selectedList?.find {
                        it == data.conversationId
                    }
                    if (checkIsSelected == null) {
                        listener.onSelectChat(position, data.conversationId, name.toString())
                    } else {
                        listener.onRemoveChat(position, data.conversationId, name.toString())
                    }
                } else {
                    listener.onSelectChat(position, data.conversationId, name.toString())
                }
            }
        }
    }

    private fun callLastReadMsgApi(
        id: String,
        isGroup: Boolean,
        msgLimit: Int,
        view: ItemGroupChatListBinding
    ) {

        val msgRequest: MessagesRequest = if (isGroup) {
            MessagesRequest.MessagesRequestBuilder()
                .setGUID(id)
                .setUnread(true)
                .setLimit(msgLimit)
                .build()
        } else {
            MessagesRequest.MessagesRequestBuilder()
                .setUID(id)
                .setUnread(true)
                .setLimit(msgLimit)
                .build()
        }

        msgRequest.fetchPrevious(object : CometChat.CallbackListener<List<BaseMessage>>() {
            override fun onSuccess(list: List<BaseMessage>?) {
                for (i in list?.reversed() ?: arrayListOf()) {
                    if (i.metadata != null) {
                        val isNull = i.metadata.isNull(Constants.IsImportant)
                        if (!isNull && isNull != null) {
                            val isImp = i.metadata.get(Constants.IsImportant) as Boolean?
                            if (isImp == true) {
                                view.tvLastMessage.setTextColor(
                                    ContextCompat.getColor(
                                        context, R.color.monza
                                    )
                                )
                                view.imgImpMsg.visibility = VISIBLE
                                listener.isShowPriorityMsg(true)
                                return
                            } else {
                                view.tvLastMessage.setTextColor(
                                    ContextCompat.getColor(
                                        context, R.color.gray
                                    )
                                )
                                view.imgImpMsg.visibility = GONE
//                                listener.isShowPriorityMsg(false)
                            }
                        } else {
                            view.tvLastMessage.setTextColor(
                                ContextCompat.getColor(
                                    context, R.color.grey
                                )
                            )
                            view.imgImpMsg.visibility = GONE
//                            listener.isShowPriorityMsg(false)
                        }
                    } else {
                        view.tvLastMessage.setTextColor(
                            ContextCompat.getColor(
                                context, R.color.grey
                            )
                        )
                        view.imgImpMsg.visibility = GONE
//                        listener.isShowPriorityMsg(false)
                    }

                }
            }

            override fun onError(e: CometChatException?) {
                Log.d("error", "Message fetching failed with exception: " + e?.message)
                view.tvLastMessage.setTextColor(
                    ContextCompat.getColor(
                        context, R.color.grey
                    )
                )
                view.imgImpMsg.visibility = GONE
                listener.isShowPriorityMsg(false)
            }

        })
    }

    fun refreshConversation(message: BaseMessage?) {
        val newConversation = CometChatHelper.getConversationFromMessage(message)
        update(newConversation)
    }

    fun update(conversation: Conversation) {
        if (conversationList.contains(conversation)) {
            val oldConversation =
                conversationList[conversationList.indexOf(conversation)]
            conversationList.remove(oldConversation)
            var isCustomMessage = false
            if (conversation.lastMessage.metadata != null && conversation.lastMessage.metadata.has("incrementUnreadCount"))
                isCustomMessage =
                    conversation.lastMessage.metadata.getBoolean("incrementUnreadCount")

            if (conversation.lastMessage.editedAt == 0L && conversation.lastMessage.deletedAt == 0L && conversation.lastMessage.category == CometChatConstants.CATEGORY_MESSAGE || isCustomMessage)
                conversation.unreadMessageCount = oldConversation.unreadMessageCount + 1
            else {
                conversation.unreadMessageCount = oldConversation.unreadMessageCount
            }
            conversationList.add(0, conversation)
        } else {
            conversationList.add(0, conversation)
        }
        notifyDataSetChanged()
    }

    fun setReadReceipts(readReceipts: MessageReceipt) {
        for (i in 0 until conversationList.size - 1) {
            val conversation = conversationList[i]
            if (conversation.conversationType == CometChatConstants.RECEIVER_TYPE_USER && readReceipts.sender.uid == (conversation.conversationWith as User).uid) {
                val baseMessage = conversationList[i].lastMessage
                if (baseMessage != null && baseMessage.readAt == 0L) {
                    baseMessage.readAt = readReceipts.readAt
                    val index = conversationList.indexOf(conversationList[i])
                    conversationList.removeAt(index)
                    conversation.lastMessage = baseMessage
                    conversationList.add(index, conversation)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun setDeliveredReceipts(deliveryReceipts: MessageReceipt) {
        for (i in 0 until conversationList.size - 1) {
            val conversation = conversationList[i]
            if (conversation.conversationType == CometChatConstants.RECEIVER_TYPE_USER && deliveryReceipts.sender.uid == (conversation.conversationWith as User).uid) {
                val baseMessage = conversationList[i].lastMessage
                if (baseMessage != null && baseMessage.deliveredAt == 0L) {
                    baseMessage.deliveredAt = deliveryReceipts.deliveredAt
                    val index = conversationList.indexOf(conversationList[i])
                    conversationList.removeAt(index)
                    conversation.lastMessage = baseMessage
                    conversationList.add(index, conversation)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun remove(conversation: Conversation?) {
        val position = conversationList.indexOf(conversation)
        conversationList.remove(conversation)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }

    interface ChatListClickManager {
        fun clickOnChatGroup(position: Int, group: Group) {}
        fun clickOnPersonalChat(position: Int, user: User) {}
        fun isShowPriorityMsg(isShow: Boolean) {}
        fun onSelectChat(position: Int, conversationId: String, name: String) {}
        fun onRemoveChat(position: Int, conversationId: String, name: String) {}
    }
}