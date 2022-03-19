package com.hipaasafe.presentation.comet_chat_main_screen.adapter

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.models.*
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.databinding.*
import com.hipaasafe.extension.Extensions
import com.hipaasafe.presentation.comet_chat_main_screen.model.MembersWithColor
import com.hipaasafe.settings.CometChatFeatureRestriction
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.CometChatUtils
import com.hipaasafe.utils.sticker_header.StickyHeaderAdapter
import org.json.JSONObject
import java.util.*


open class MainChatAdapter(
    val context: Context,
    private val msgList: ArrayList<BaseMessage>,
    val membersList: ArrayList<MembersWithColor>,
    var listener: MessageClickManager
) :
    RecyclerView.Adapter<MainChatAdapter.ViewHolder>(),
    StickyHeaderAdapter<MainChatAdapter.DateItemHolder?> {

    private val loggedInUser = CometChat.getLoggedInUser()
    private var isLinkPreview: Boolean = false

    companion object {
        const val RIGHT_IMAGE_MESSAGE = 56
        const val LEFT_IMAGE_MESSAGE = 89
        const val RIGHT_VIDEO_MESSAGE = 78
        const val LEFT_VIDEO_MESSAGE = 87
        const val RIGHT_AUDIO_MESSAGE = 39
        const val LEFT_AUDIO_MESSAGE = 93
        const val CALL_MESSAGE = 234
        const val LEFT_TEXT_MESSAGE = 1
        const val RIGHT_TEXT_MESSAGE = 2
        const val RIGHT_FILE_MESSAGE = 23
        const val LEFT_FILE_MESSAGE = 25
        const val ACTION_MESSAGE = 99
        const val RIGHT_LINK_MESSAGE = 12
        const val LEFT_LINK_MESSAGE = 13
        const val LEFT_DELETE_MESSAGE = 551
        const val RIGHT_DELETE_MESSAGE = 552
        const val RIGHT_CUSTOM_MESSAGE = 432
        const val LEFT_CUSTOM_MESSAGE = 431
        const val RIGHT_REPLY_TEXT_MESSAGE = 987
        const val LEFT_REPLY_TEXT_MESSAGE = 789
        const val LEFT_LOCATION_CUSTOM_MESSAGE = 31
        const val RIGHT_LOCATION_CUSTOM_MESSAGE = 32
        const val RIGHT_STICKER_MESSAGE = 21
        const val LEFT_STICKER_MESSAGE = 22
        const val LEFT_WHITEBOARD_MESSAGE = 7
        const val RIGHT_WHITEBOARD_MESSAGE = 8
        const val LEFT_WRITEBOARD_MESSAGE = 9
        const val RIGHT_WRITEBOARD_MESSAGE = 10
        const val LEFT_CONFERENCE_CALL_MESSAGE = 51
        const val RIGHT_CONFERENCE_CALL_MESSAGE = 52
        const val RIGHT_POLLS_CUSTOM_MESSAGE = 14
        const val LEFT_POLLS_CUSTOM_MESSAGE = 15

        var LATITUDE = 0.0
        var LONGITUDE = 0.0
    }

    init {
        CometChatFeatureRestriction.isLinkPreviewEnabled(object :
            CometChatFeatureRestriction.OnSuccessListener {
            override fun onSuccess(p0: Boolean) {
                isLinkPreview = p0
            }
        })
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var txtMsgLeft: ItemMsgIncomingBinding? = null
        var txtMsgRight: ItemMsgOutgoingBinding? = null
        var actionMsg: ItemActionMsgBinding? = null

        //        var imgRight: ItemMediaImageRightBinding? = null
//        var imgLeft: ItemMediaImageLeftBinding? = null
//        var videoRight: ItemMediaVideoRightBinding? = null
//        var videoLeft: ItemMediaVideoLeftBinding? = null
//        var audioLeft: ItemMediaMusicLeftBinding? = null
//        var audioRight: ItemMediaMusicRightBinding? = null
//        var fileRight: ItemMediaFileRightBinding? = null
//        var fileLeft: ItemMediaFileLeftBinding? = null
//        var locationRight: ItemLocationRightBinding? = null
//        var locationLeft: ItemLocationLeftBinding? = null
        var replyTxtRight: ItemTextReplyRightBinding? = null
        var replyTxtLeft: ItemTextReplyLeftBinding? = null
        var deleteMsgRightBinding: ItemDeleteMsgRightBinding? = null
        var deleteMsgLeftBinding: ItemDeleteMsgLeftBinding? = null


        constructor(binding: ItemDeleteMsgLeftBinding) : super(binding.root) {
            deleteMsgLeftBinding = binding
        }

        constructor(binding: ItemDeleteMsgRightBinding) : super(binding.root) {
            deleteMsgRightBinding = binding
        }

        constructor(binding: ItemTextReplyLeftBinding) : super(binding.root) {
            replyTxtLeft = binding
        }

        constructor(binding: ItemTextReplyRightBinding) : super(binding.root) {
            replyTxtRight = binding
        }

        constructor(binding: ItemMsgIncomingBinding) : super(binding.root) {
            txtMsgLeft = binding
        }

        constructor(binding: ItemMsgOutgoingBinding) : super(binding.root) {
            txtMsgRight = binding
        }

        constructor(binding: ItemActionMsgBinding) : super(binding.root) {
            actionMsg = binding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            LEFT_TEXT_MESSAGE -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.item_msg_incoming, parent, false)
                val binding = ItemMsgIncomingBinding.bind(view)
                ViewHolder(binding)
            }
            RIGHT_TEXT_MESSAGE -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.item_msg_outgoing, parent, false)
                val binding = ItemMsgOutgoingBinding.bind(view)
                ViewHolder(binding)
            }
            LEFT_REPLY_TEXT_MESSAGE -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_text_reply_left, parent, false)
                val binding = ItemTextReplyLeftBinding.bind(view)
                ViewHolder(binding)
            }
            RIGHT_REPLY_TEXT_MESSAGE -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_text_reply_right, parent, false)
                val binding = ItemTextReplyRightBinding.bind(view)
                ViewHolder(binding)
            }
            LEFT_DELETE_MESSAGE -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_delete_msg_left, parent, false)
                val binding = ItemDeleteMsgLeftBinding.bind(view)
                ViewHolder(binding)
            }
            RIGHT_DELETE_MESSAGE -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_delete_msg_right, parent, false)
                val binding = ItemDeleteMsgRightBinding.bind(view)
                ViewHolder(binding)
            }
            ACTION_MESSAGE -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.item_action_msg, parent, false)
                val binding = ItemActionMsgBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.item_action_msg, parent, false)
                val binding = ItemActionMsgBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = msgList[position]
        when (holder.itemViewType) {
            LEFT_TEXT_MESSAGE -> {
                val baseMessage = data as TextMessage
                holder.txtMsgLeft?.apply {
                    checkImpMsg(
                        baseMessage = baseMessage,
                        view = containerMsg,
                        textView = tvMsg,
                        impIcon = imgImp
                    )
                    if (data.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                        tvSenderName.apply {
                            visibility = VISIBLE
                            text = baseMessage.sender.name
                            setUserNameColor(uid = baseMessage.sender.uid, userNameView = this)
                        }
                    } else {
                        tvSenderName.visibility = GONE
                    }
                    tvMsg.text = baseMessage.text
                    setStatusIcon(txtTime = tvDate, statusIcon = null, baseMessage)
                    if (baseMessage.editedAt != 0L) {
                        tvEdited.visibility = VISIBLE
                        tvEditDate.visibility = VISIBLE
                        tvEditDate.text =
                            AppUtils.INSTANCE?.getHeaderDate(baseMessage.editedAt * 1000)
                    } else {
                        tvEdited.visibility = GONE
                        tvEditDate.visibility = GONE
                    }
                    tvMsg.handleUrlClicks {
                        listener.clickOnAttachmentLink(position,it)
                    }
                }

                holder.itemView.apply {
                    setOnLongClickListener {
                        listener.longClickOnTextMsg(
                            position,
                            baseMessage,
                            msgType = LEFT_TEXT_MESSAGE
                        )
                        true
                    }
                    setOnClickListener {
                        listener.clickOnTextMsg(position, baseMessage = baseMessage)
                    }
                }
            }
            RIGHT_TEXT_MESSAGE -> {
                val baseMessage = data as TextMessage
                holder.txtMsgRight?.apply {
                    checkImpMsg(
                        baseMessage = baseMessage,
                        view = containerMsg,
                        textView = tvMsg,
                        impIcon = imgImp
                    )
                    tvMsg.text = baseMessage.text
                    setStatusIcon(tvDate, imgMsgStatus, baseMessage)
                    if (baseMessage.editedAt != 0L) {
                        tvEdited.visibility = VISIBLE
                        tvEditDate.visibility = VISIBLE
                        tvEditDate.text =
                            AppUtils.INSTANCE?.getHeaderDate(baseMessage.editedAt * 1000)
                    } else {
                        tvEdited.visibility = GONE
                        tvEditDate.visibility = GONE
                    }
                    tvMsg.handleUrlClicks {
                        listener.clickOnAttachmentLink(position,it)
                    }

                }

                holder.itemView.apply {
                    setOnClickListener {
                        listener.clickOnTextMsg(position, baseMessage = baseMessage)
                    }
                    setOnLongClickListener {
                        listener.longClickOnTextMsg(
                            position,
                            baseMessage,
                            msgType = RIGHT_TEXT_MESSAGE
                        )
                        true
                    }
                }
            }

            LEFT_REPLY_TEXT_MESSAGE -> {
                val baseMessage = data as TextMessage
                holder.replyTxtLeft?.apply {
                    checkImpMsg(
                        baseMessage = baseMessage,
                        view = containerMsg,
                        textView = tvMsg,
                        impIcon = imgImp
                    )
                    if (data.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                        tvSenderName.apply {
                            visibility = VISIBLE
                            text = baseMessage.sender.name
                            setUserNameColor(uid = baseMessage.sender.uid, userNameView = this)
                        }
                    } else {
                        tvSenderName.visibility = GONE
                    }
                    tvMsg.text = baseMessage.text
                    setStatusIcon(txtTime = tvDate, statusIcon = null, baseMessage)
                    if (baseMessage.editedAt != 0L) {
                        tvEdited.visibility = VISIBLE
                    } else {
                        tvEdited.visibility = GONE
                    }

                    replyLayout.root.setOnClickListener {
                        listener.clickOnReplyMsg(position, baseMessage = baseMessage)
                    }

                    if ((baseMessage.metadata != null && baseMessage.metadata.has("reply-message")) || (baseMessage.metadata != null && baseMessage.metadata.has(
                            "replyToMessage"
                        ))
                    ) {
                        try {
                            var metaData = JSONObject()
                            if (baseMessage.metadata.has("reply-message"))
                                metaData = baseMessage.metadata.getJSONObject("reply-message")
                            else if (baseMessage.metadata.has("replyToMessage"))
                                metaData = baseMessage.metadata.getJSONObject("replyToMessage")
                            val messageType = metaData.getString("type")
                            val data = metaData.getJSONObject("data")
                            var replyMessage = ""
                            if (data.has("text"))
                                replyMessage = data.getString("text")

                            if (data.has("metadata")) {
                                val isImp = data.getJSONObject("metadata")
                                if (isImp.has(Constants.IsImportant)) {
                                    val imp = isImp.getBoolean(Constants.IsImportant)
                                    if (imp) {
                                        replyLayout.apply {
                                            layoutReply.setCardBackgroundColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.monza
                                                )
                                            )
                                            containerLayoutReply.backgroundTintList =
                                                ContextCompat.getColorStateList(
                                                    context,
                                                    R.color.lavender_blush
                                                )
                                            tvReplyMsg.setTextColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.monza
                                                )
                                            )
                                        }
                                    } else {
                                        replyLayout.apply {
                                            layoutReply.setCardBackgroundColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.purple_heart
                                                )
                                            )
                                            containerLayoutReply.backgroundTintList =
                                                ContextCompat.getColorStateList(
                                                    context,
                                                    R.color.tropical_blue
                                                )
                                            tvReplyMsg.setTextColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.silver_chalice
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            replyLayout.apply {
                                tvReplyMsgTitle.text = CometChatUtils.getSenderName(data)
                                tvReplyMsgTitle.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.purple_heart
                                    )
                                )

                                when (messageType) {
                                    CometChatConstants.MESSAGE_TYPE_TEXT -> {
                                        tvReplyMsg.text = replyMessage
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_IMAGE -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_image)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_AUDIO -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_audio)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_VIDEO -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_video)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_FILE -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_file)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    Constants.CometChatConstant.LOCATION -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.custom_message_location)
                                        imgReplyMsg.visibility =
                                            VISIBLE
                                    }
                                    Constants.CometChatConstant.STICKERS -> {
                                        tvReplyMsg.text =
                                            context.getString(R.string.custom_message_sticker)
                                        imgReplyMsg.visibility = GONE
                                    }

                                    Constants.CometChatConstant.MEETING -> {
                                        tvReplyMsg.text =
                                            context.getString(R.string.custom_message_meeting)
                                        imgReplyMsg.visibility = GONE
                                    }
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            Log.e("TAG", "setTextData: " + e.message)
                        }
                    }
                }

                holder.itemView.apply {
                    setOnLongClickListener {
                        listener.longClickOnReplyMsg(
                            position,
                            baseMessage,
                            msgType = LEFT_REPLY_TEXT_MESSAGE
                        )
                        true
                    }
                }
            }

            RIGHT_REPLY_TEXT_MESSAGE -> {
                val baseMessage = data as TextMessage
                holder.replyTxtRight?.apply {
                    checkImpMsg(
                        baseMessage = baseMessage,
                        view = containerMsg,
                        textView = tvMsg,
                        impIcon = imgImp
                    )
                    tvMsg.text = baseMessage.text
                    setStatusIcon(tvDate, imgMsgStatus, baseMessage)
                    if (baseMessage.editedAt != 0L) {
                        tvEdited.visibility = VISIBLE
                    } else {
                        tvEdited.visibility = GONE
                    }

                    replyLayout.root.setOnClickListener {
                        listener.clickOnReplyMsg(position, baseMessage = baseMessage)
                    }

                    if ((baseMessage.metadata != null && baseMessage.metadata.has("reply-message")) || (baseMessage.metadata != null && baseMessage.metadata.has(
                            "replyToMessage"
                        ))
                    ) {
                        try {
                            var metaData = JSONObject()
                            if (baseMessage.metadata.has("reply-message"))
                                metaData = baseMessage.metadata.getJSONObject("reply-message")
                            else if (baseMessage.metadata.has("replyToMessage"))
                                metaData = baseMessage.metadata.getJSONObject("replyToMessage")
                            val messageType = metaData.getString("type")
                            val data = metaData.getJSONObject("data")
                            var replyMessage = ""
                            if (data.has("text"))
                                replyMessage = data.getString("text")

                            if (data.has("metadata")) {
                                val isImp = data.getJSONObject("metadata")
                                if (isImp.has(Constants.IsImportant)) {
                                    val imp = isImp.getBoolean(Constants.IsImportant)
                                    if (imp) {
                                        replyLayout.apply {
                                            layoutReply.setCardBackgroundColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.monza
                                                )
                                            )
                                            containerLayoutReply.backgroundTintList =
                                                ContextCompat.getColorStateList(
                                                    context,
                                                    R.color.lavender_blush
                                                )
                                            tvReplyMsg.setTextColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.monza
                                                )
                                            )
                                        }
                                    } else {
                                        replyLayout.apply {
                                            layoutReply.setCardBackgroundColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.medium_purple
                                                )
                                            )
                                            containerLayoutReply.backgroundTintList =
                                                ContextCompat.getColorStateList(
                                                    context,
                                                    R.color.tropical_blue
                                                )
                                            tvReplyMsg.setTextColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.silver_chalice
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            replyLayout.apply {
                                tvReplyMsgTitle.text = CometChatUtils.getSenderName(data)
                                tvReplyMsgTitle.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.medium_purple
                                    )
                                )
                                when (messageType) {
                                    CometChatConstants.MESSAGE_TYPE_TEXT -> {
                                        tvReplyMsg.text = replyMessage
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_IMAGE -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_image)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_AUDIO -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_audio)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_VIDEO -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_video)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    CometChatConstants.MESSAGE_TYPE_FILE -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.message_file)
                                        imgReplyMsg.visibility = GONE
                                    }
                                    Constants.CometChatConstant.LOCATION -> {
                                        tvReplyMsg.text =
                                            context.resources.getString(R.string.custom_message_location)
                                        imgReplyMsg.visibility =
                                            VISIBLE
                                    }
                                    Constants.CometChatConstant.STICKERS -> {
                                        tvReplyMsg.text =
                                            context.getString(R.string.custom_message_sticker)
                                        imgReplyMsg.visibility = GONE
                                    }

                                    Constants.CometChatConstant.MEETING -> {
                                        tvReplyMsg.text =
                                            context.getString(R.string.custom_message_meeting)
                                        imgReplyMsg.visibility = GONE
                                    }
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            Log.e("TAG", "setTextData: " + e.message)
                        }
                    }


                }

                holder.itemView.apply {
                    setOnLongClickListener {
                        listener.longClickOnReplyMsg(
                            position,
                            baseMessage,
                            msgType = RIGHT_REPLY_TEXT_MESSAGE
                        )
                        true
                    }
                }
            }

            LEFT_DELETE_MESSAGE -> {
                holder.deleteMsgLeftBinding?.apply {
                    if (data.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                        tvSenderName.apply {
                            visibility = VISIBLE
                            text = data.sender.name
                            setUserNameColor(uid = data.sender.uid, userNameView = this)
                        }
                    } else {
                        tvSenderName.visibility = GONE
                    }
                    setStatusIcon(txtTime = tvDate, statusIcon = null, data)
                }
            }

            RIGHT_DELETE_MESSAGE -> {
                holder.deleteMsgRightBinding?.apply {
                    setStatusIcon(txtTime = tvDate, statusIcon = null, data)
                }
            }

            LEFT_IMAGE_MESSAGE -> {

            }
            RIGHT_IMAGE_MESSAGE -> {

            }

            LEFT_VIDEO_MESSAGE -> {

            }
            RIGHT_VIDEO_MESSAGE -> {

            }

            LEFT_AUDIO_MESSAGE -> {

            }

            RIGHT_AUDIO_MESSAGE -> {

            }

            LEFT_FILE_MESSAGE -> {

            }
            RIGHT_FILE_MESSAGE -> {

            }

            LEFT_LOCATION_CUSTOM_MESSAGE -> {

            }

            RIGHT_LOCATION_CUSTOM_MESSAGE -> {

            }

            ACTION_MESSAGE, CALL_MESSAGE -> {
                holder.actionMsg?.apply {
                    if (data is Action) {
                        var actionMessage: String? = ""
                        if (data.action == CometChatConstants.ActionKeys.ACTION_JOINED)
                            actionMessage =
                                if (loggedInUser.uid == (data.actioBy as User).uid) "You " + context.getString(
                                    R.string.joined
                                ) else
                                    (data.actioBy as User).name + " " + context.getString(R.string.joined)
                        else if (data.action == CometChatConstants.ActionKeys.ACTION_MEMBER_ADDED) actionMessage =
                            if (loggedInUser.uid == (data.actioBy as User).uid) "You " + context.getString(
                                R.string.added
                            ) + " " + (data.actionOn as User).name
                            else if (loggedInUser.uid == (data.actionOn as User).uid) ((data.actioBy as User).name + " "
                                    + context.getString(R.string.added) + " You")
                            else
                                ((data.actioBy as User).name + " "
                                        + context.getString(R.string.added) + " " + (data.actionOn as User).name)
                        else if (data.action == CometChatConstants.ActionKeys.ACTION_KICKED) actionMessage =
                            if (loggedInUser.uid == (data.actioBy as User).uid) "You " + context.getString(
                                R.string.removed
                            ) + " " + (data.actionOn as User).name
                            else if (loggedInUser.uid == (data.actionOn as User).uid) ((data.actioBy as User).name + " "
                                    + context.getString(R.string.removed) + " You")
                            else
                                ((data.actioBy as User).name + " "
                                        + context.getString(R.string.removed) + " " + (data.actionOn as User).name)
                        else if (data.action == CometChatConstants.ActionKeys.ACTION_BANNED) actionMessage =
                            if (loggedInUser.uid == (data.actioBy as User).uid) "You " + context.getString(
                                R.string.blocked
                            ) + " " + (data.actionOn as User).name
                            else if (loggedInUser.uid == (data.actionOn as User).uid) ((data.actioBy as User).name + " "
                                    + context.getString(R.string.blocked) + " You")
                            else
                                ((data.actioBy as User).name + " "
                                        + context.getString(R.string.blocked) + " " + (data.actionOn as User).name)
                        else if (data.action == CometChatConstants.ActionKeys.ACTION_UNBANNED) actionMessage =
                            if (loggedInUser.uid == (data.actioBy as User).uid) "You " + context.getString(
                                R.string.unblocked
                            ) + " " + (data.actionOn as User).name
                            else if (loggedInUser.uid == (data.actionOn as User).uid) ((data.actioBy as User).name + " "
                                    + context.getString(R.string.unblocked) + " You")
                            else
                                ((data.actioBy as User).name + " "
                                        + context.getString(R.string.unblocked) + " " + (data.actionOn as User).name)
                        else if (data.action == CometChatConstants.ActionKeys.ACTION_LEFT) actionMessage =
                            if (loggedInUser.uid == (data.actioBy as User).uid) " You " + context.getString(
                                R.string.left
                            ) else
                                (data.actioBy as User).name + " " + context.getString(R.string.left)
                        else if (data.action == CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED)
                            actionMessage =
                                if (data.newScope == CometChatConstants.SCOPE_MODERATOR) {
                                    if (loggedInUser.uid == (data.actioBy as User).uid) " You " + context.getString(
                                        R.string.made
                                    ) + " " + (data.actionOn as User).name + " " + context.getString(
                                        R.string.moderator
                                    )
                                    else if (loggedInUser.uid == (data.actionOn as User).uid) ((data.actioBy as User).name + " " + context.getString(
                                        R.string.made
                                    ) + " You " + context.getString(R.string.moderator))
                                    else
                                        ((data.actioBy as User).name + " " + context.getString(
                                            R.string.made
                                        ) + " "
                                                + (data.actionOn as User).name + " " + context.getString(
                                            R.string.moderator
                                        ))
                                } else if (data.newScope == CometChatConstants.SCOPE_ADMIN) {
                                    if (loggedInUser.uid == (data.actioBy as User).uid) " You " + context.getString(
                                        R.string.made
                                    ) + " " + (data.actionOn as User).name + " " + context.getString(
                                        R.string.admin
                                    )
                                    else if (loggedInUser.uid == (data.actionOn as User).uid) ((data.actioBy as User).name + context.getString(
                                        R.string.made
                                    ) + " You " + context.getString(R.string.admin))
                                    else
                                        ((data.actioBy as User).name + " " + context.getString(
                                            R.string.made
                                        ) + " "
                                                + (data.actionOn as User).name + " " + context.getString(
                                            R.string.admin
                                        ))
                                } else if (data.newScope == CometChatConstants.SCOPE_PARTICIPANT) {
                                    if (loggedInUser.uid == (data.actioBy as User).uid) "You " + context.getString(
                                        R.string.made
                                    ) + " " + (data.actionOn as User).name + " " + context.getString(
                                        R.string.participant
                                    )
                                    else if (loggedInUser.uid == (data.actionOn as User).uid) ((data.actioBy as User).name + context.getString(
                                        R.string.made
                                    ) + " You " + context.getString(R.string.participant))
                                    else
                                        ((data.actioBy as User).name + " " + context.getString(
                                            R.string.made
                                        ) + " "
                                                + (data.actionOn as User).name + " " + context.getString(
                                            R.string.participant
                                        ))
                                } else data.message
                        tvAction.text = actionMessage
                    } else if (data is Call) {
                        val call = data as Call
                        var callMessageText = ""
                        var isMissed = false
                        var isIncoming = false
                        var isVideo = false
                        if (call.callStatus == CometChatConstants.CALL_STATUS_INITIATED) {
                            callMessageText =
                                if (loggedInUser.uid == call.sender.uid) "You " + context.getString(
                                    R.string.initiated
                                ) else call.sender.name + " " + context.getString(
                                    R.string.initiated
                                )
                        } else if (call.callStatus == CometChatConstants.CALL_STATUS_UNANSWERED || call.callStatus == CometChatConstants.CALL_STATUS_CANCELLED) {
                            callMessageText =
                                context.resources.getString(R.string.missed_call)
                            isMissed = true
                        } else if (call.callStatus == CometChatConstants.CALL_STATUS_REJECTED) {
                            callMessageText =
                                context.resources.getString(R.string.rejected_call)
                        } else if (call.callStatus == CometChatConstants.CALL_STATUS_ONGOING) {
                            callMessageText =
                                context.getString(R.string.ongoing)
                        } else if (call.callStatus == CometChatConstants.CALL_STATUS_ENDED) {
                            callMessageText =
                                context.getString(R.string.ended)
                        } else {
                            callMessageText = call.callStatus
                        }

                        if (call.type == CometChatConstants.CALL_TYPE_VIDEO) {
                            callMessageText =
                                callMessageText + " " + context.resources.getString(R.string.video_call)
                            isVideo = true
                        } else {
                            callMessageText =
                                callMessageText + " " + context.resources.getString(R.string.audio_call)
                            isVideo = false
                        }
                        tvAction.text = callMessageText
                    }
                }
            }

            else -> {
                val e = ""
            }
        }
    }


    private fun setUserNameColor(uid: String, userNameView: TextView) {
        val membersWithColor = membersList.find {
            it.uid == uid
        }
        if (membersWithColor != null) {
            val index = membersList.indexOf(membersWithColor)
            membersList[index].color?.let { userNameView.setTextColor(it) }
        }
    }

    private fun checkImpMsg(
        baseMessage: BaseMessage,
        view: View,
        textView: TextView,
        impIcon: ImageView
    ) {
        if (baseMessage.sender == loggedInUser) {
            if (baseMessage.metadata != null) {
                val isNull = baseMessage.metadata?.isNull(Constants.IsImportant)
                if (isNull != true && isNull != null) {
                    val isImp: Boolean =
                        baseMessage.metadata.getBoolean(Constants.IsImportant)
                    if (isImp && isImp != null) {
                        view.setBackgroundResource(R.drawable.bg_chat_imp)
                        textView.setTextColor(ContextCompat.getColor(context, R.color.monza))
                        //todo visible it when required
//                        impIcon.visibility = VISIBLE
                    } else {
                        view.setBackgroundResource(R.drawable.bg_chat_text)
                        textView.setTextColor(ContextCompat.getColor(context, R.color.codGray))
                        impIcon.visibility = GONE
                    }
                } else {
                    view.setBackgroundResource(R.drawable.bg_chat_text)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.codGray))
                    impIcon.visibility = GONE
                }
            } else {
                view.setBackgroundResource(R.drawable.bg_chat_text)
                textView.setTextColor(ContextCompat.getColor(context, R.color.codGray))
                impIcon.visibility = GONE
            }
        } else {
            if (baseMessage.metadata != null) {
                val isNull = baseMessage.metadata?.isNull(Constants.IsImportant)
                if (isNull != true && isNull != null) {
                    val isImp: Boolean =
                        baseMessage.metadata.getBoolean(Constants.IsImportant)
                    if (isImp && isImp != null) {
                        view.setBackgroundResource(R.drawable.bg_chat_imp)
                        textView.setTextColor(ContextCompat.getColor(context, R.color.monza))
                        //todo visible it when required
//                        impIcon.visibility = VISIBLE
                    } else {
                        view.setBackgroundResource(R.drawable.bg_chat_text2)
                        textView.setTextColor(ContextCompat.getColor(context, R.color.codGray))
                        impIcon.visibility = GONE
                    }
                } else {
                    view.setBackgroundResource(R.drawable.bg_chat_text2)
                    textView.setTextColor(ContextCompat.getColor(context, R.color.codGray))
                    impIcon.visibility = GONE
                }
            } else {
                view.setBackgroundResource(R.drawable.bg_chat_text2)
                textView.setTextColor(ContextCompat.getColor(context, R.color.codGray))
                impIcon.visibility = GONE
            }
        }
    }

    private fun setStatusIcon(txtTime: TextView, statusIcon: ImageView?, baseMessage: BaseMessage) {
        CometChatFeatureRestriction.isDeliveryReceiptsEnabled(object :
            CometChatFeatureRestriction.OnSuccessListener {
            override fun onSuccess(p0: Boolean) {
                if (p0) {
                    if (baseMessage.sender.uid == loggedInUser.uid) {
                        if (baseMessage.receiverType == CometChatConstants.RECEIVER_TYPE_USER) {
                            if (baseMessage.readAt != 0L) {
                                txtTime.text =
                                    AppUtils.INSTANCE?.getHeaderDate(baseMessage.readAt * 1000)
                                statusIcon?.setImageResource(R.drawable.ic_read_msg)
                            } else if (baseMessage.deliveredAt != 0L) {
                                txtTime.text =
                                    AppUtils.INSTANCE?.getHeaderDate(baseMessage.deliveredAt * 1000)
                                statusIcon?.setImageResource(R.drawable.ic_deliver_msg)
                            } else if (baseMessage.sentAt > 0) {
                                txtTime.text =
                                    AppUtils.INSTANCE?.getHeaderDate(baseMessage.sentAt * 1000)
                                statusIcon?.setImageResource(R.drawable.ic_send_msg)
                            } else if (baseMessage.sentAt == -1L) {
                                txtTime.text = ""
                                statusIcon?.setImageResource(R.drawable.ic_error)
                            } else {
                                txtTime.text =
                                    AppUtils.INSTANCE?.getHeaderDate(baseMessage.sentAt * 1000)
                                statusIcon?.setImageResource(R.drawable.ic_send_msg)
                            }
                        } else {
                            if (baseMessage.sentAt > 0) {
                                txtTime.text =
                                    AppUtils.INSTANCE?.getHeaderDate(baseMessage.sentAt * 1000)
                                statusIcon?.visibility = GONE
                            } else {
                                txtTime.text = ""
                                statusIcon?.visibility = VISIBLE
                                statusIcon?.setImageResource(R.drawable.ic_wait)
                            }
                        }
                    } else {
                        txtTime.text = AppUtils.INSTANCE?.getHeaderDate(baseMessage.sentAt * 1000)
                    }
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        return getItemViewTypes(position)
    }

    fun updateList(baseMessageList: List<BaseMessage>) {
        setMessageList(baseMessageList)
    }

    fun addMessage(baseMessage: BaseMessage) {
        msgList.add(baseMessage)
        notifyItemInserted(msgList.size - 1)
    }

    fun setUpdatedMessage(baseMessage: BaseMessage) {
        if (msgList.contains(baseMessage)) {
            val index = msgList.indexOf(baseMessage)
            msgList.remove(baseMessage)
            msgList.add(index, baseMessage)
            notifyItemChanged(index)
        }
    }

    fun setReadReceipts(messageReceipt: MessageReceipt) {
        for (i in msgList.indices.reversed()) {
            val baseMessage = msgList[i]
            if (baseMessage.readAt == 0L) {
                val index = msgList.indexOf(baseMessage)
                msgList[index].readAt = messageReceipt.readAt
            }
        }
        notifyDataSetChanged()
    }

    fun setDeliveryReceipts(messageReceipt: MessageReceipt) {
        for (i in msgList.indices.reversed()) {
            val baseMessage = msgList[i]
            if (baseMessage.deliveredAt == 0L) {
                val index = msgList.indexOf(baseMessage)
                msgList[index].deliveredAt = messageReceipt.deliveredAt
            }
        }
        notifyDataSetChanged()
    }

    fun updateSentMessage(baseMessage: BaseMessage?) {
        for (i in msgList.indices.reversed()) {
            val mUid = msgList[i].muid
            if (mUid != null && mUid == baseMessage?.muid) {
                msgList.removeAt(i)
                msgList.add(i, baseMessage)
                notifyItemChanged(i)
            }
        }
    }

    fun removeMessage(baseMessage: BaseMessage) {
        if (msgList.contains(baseMessage)) {
            val index = msgList.indexOf(baseMessage)
            msgList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun setMessageList(messageList: List<BaseMessage>) {
        msgList.addAll(0, messageList)
        notifyItemRangeInserted(0, messageList.size)
    }

    private fun getItemViewTypes(position: Int): Int {
        val baseMessage = msgList[position]
        val extensionList = Extensions.extensionCheck(baseMessage)
        if (baseMessage.deletedAt == 0L) {
            if (baseMessage.category == CometChatConstants.CATEGORY_MESSAGE) {
                return when (baseMessage.type) {
                    CometChatConstants.MESSAGE_TYPE_TEXT -> if (baseMessage.sender.uid == loggedInUser.uid) {
                        if (isLinkPreview && extensionList != null && extensionList.containsKey("linkPreview") && extensionList["linkPreview"] != null) RIGHT_LINK_MESSAGE
                        else if (baseMessage.metadata != null && baseMessage.metadata.has("reply-message") || baseMessage.metadata != null && baseMessage.metadata.has(
                                "replyToMessage"
                            )
                        )
                            return RIGHT_REPLY_TEXT_MESSAGE
                        else RIGHT_TEXT_MESSAGE

                    } else {
                        if (isLinkPreview && extensionList != null && extensionList.containsKey("linkPreview") && extensionList["linkPreview"] != null) LEFT_LINK_MESSAGE
                        else if (baseMessage.metadata != null && baseMessage.metadata.has("reply-message") || baseMessage.metadata != null && baseMessage.metadata.has(
                                "replyToMessage"
                            )
                        )
                            return LEFT_REPLY_TEXT_MESSAGE
                        else LEFT_TEXT_MESSAGE
                    }
                    CometChatConstants.MESSAGE_TYPE_AUDIO -> if (baseMessage.sender.uid == loggedInUser.uid) {
                        RIGHT_AUDIO_MESSAGE
                    } else {
                        LEFT_AUDIO_MESSAGE
                    }
                    CometChatConstants.MESSAGE_TYPE_IMAGE -> if (baseMessage.sender.uid == loggedInUser.uid) {
                        RIGHT_IMAGE_MESSAGE
                    } else {
                        LEFT_IMAGE_MESSAGE
                    }
                    CometChatConstants.MESSAGE_TYPE_VIDEO -> if (baseMessage.sender.uid == loggedInUser.uid) {
                        RIGHT_VIDEO_MESSAGE
                    } else {
                        LEFT_VIDEO_MESSAGE
                    }
                    CometChatConstants.MESSAGE_TYPE_FILE -> if (baseMessage.sender.uid == loggedInUser.uid) {
                        RIGHT_FILE_MESSAGE
                    } else {
                        LEFT_FILE_MESSAGE
                    }
                    else -> -1
                }
            } else {
                if (baseMessage.category == CometChatConstants.CATEGORY_ACTION) {
                    if (baseMessage is Action && CometChatFeatureRestriction.isGroupActionMessagesEnabled())
                        return ACTION_MESSAGE
                } else if (baseMessage.category == CometChatConstants.CATEGORY_CALL) {
                    if (baseMessage is Call && CometChatFeatureRestriction.isCallActionMessagesEnabled())
                        return CALL_MESSAGE
                } else if (baseMessage.category == CometChatConstants.CATEGORY_CUSTOM) {
                    if (baseMessage.sender.uid == loggedInUser.uid) {
                        return when (baseMessage.type) {
                            Constants.CometChatConstant.LOCATION -> RIGHT_LOCATION_CUSTOM_MESSAGE
                            Constants.CometChatConstant.STICKERS -> RIGHT_STICKER_MESSAGE
                            Constants.CometChatConstant.WHITEBOARD -> RIGHT_WHITEBOARD_MESSAGE
                            Constants.CometChatConstant.WRITEBOARD -> RIGHT_WRITEBOARD_MESSAGE
                            Constants.CometChatConstant.MEETING -> RIGHT_CONFERENCE_CALL_MESSAGE
                            else -> RIGHT_CUSTOM_MESSAGE
                        }
                    } else {
                        return when (baseMessage.type) {
                            Constants.CometChatConstant.LOCATION -> LEFT_LOCATION_CUSTOM_MESSAGE
                            Constants.CometChatConstant.STICKERS -> LEFT_STICKER_MESSAGE
                            Constants.CometChatConstant.WHITEBOARD -> LEFT_WHITEBOARD_MESSAGE
                            Constants.CometChatConstant.WRITEBOARD -> LEFT_WRITEBOARD_MESSAGE
                            Constants.CometChatConstant.MEETING -> LEFT_CONFERENCE_CALL_MESSAGE

                            else -> LEFT_CUSTOM_MESSAGE
                        }
                    }
                }
            }
        } else {
            return if (baseMessage.sender.uid == loggedInUser.uid) {
                RIGHT_DELETE_MESSAGE
            } else {
                LEFT_DELETE_MESSAGE
            }
        }
        return -1
    }

    inner class DateItemHolder(val dateBinding: ItemDateSeparatorBinding) :
        RecyclerView.ViewHolder(dateBinding.root)

    interface MessageClickManager {
        fun clickOnTextMsg(position: Int, baseMessage: BaseMessage)
        fun clickOnImageMsg(position: Int, baseMessage: BaseMessage)
        fun clickOnVideoMsg(position: Int, baseMessage: BaseMessage)
        fun clickOnFileMsg(position: Int, baseMessage: BaseMessage)
        fun clickOnMapMsg(position: Int, baseMessage: BaseMessage)
        fun clickOnAudioMsg(position: Int, baseMessage: BaseMessage)
        fun clickOnReplyMsg(position: Int, baseMessage: BaseMessage)
        fun clickOnAttachmentLink(position: Int,link:String)

        fun longClickOnTextMsg(position: Int, baseMessage: BaseMessage, msgType: Int)
        fun longClickOnImageMsg(position: Int, baseMessage: BaseMessage, msgType: Int)
        fun longClickOnVideoMsg(position: Int, baseMessage: BaseMessage, msgType: Int)
        fun longClickOnFileMsg(position: Int, baseMessage: BaseMessage, msgType: Int)
        fun longClickOnMapMsg(position: Int, baseMessage: BaseMessage, msgType: Int)
        fun longClickOnAudioMsg(position: Int, baseMessage: BaseMessage, msgType: Int)
        fun longClickOnReplyMsg(position: Int, baseMessage: BaseMessage, msgType: Int)
    }

    override fun getHeaderId(var1: Int): Long {
        val baseMessage = msgList[var1]
        return AppUtils.INSTANCE?.getDateId(baseMessage.sentAt * 1000)!!.toLong()
    }

    override fun onCreateHeaderViewHolder(var1: ViewGroup?): DateItemHolder {
        val view = LayoutInflater.from(var1?.context).inflate(
            R.layout.item_date_separator,
            var1, false
        )
        val binding = ItemDateSeparatorBinding.bind(view)
        return DateItemHolder(binding)
    }

    override fun onBindHeaderViewHolder(var1: Any, var2: Int, var3: Long) {
        val baseMessage = msgList[var2]
        val date = Date(baseMessage.sentAt * 1000L)
        val formattedDate = AppUtils.INSTANCE?.getDate(date.time)
        val dateItemHolder = var1 as DateItemHolder
        dateItemHolder.dateBinding.textDate.apply {
            if (formattedDate != "01/01/1970") {
                visibility = VISIBLE
                text = formattedDate
            } else {
                visibility = GONE
            }
        }
    }

    private fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
        //create span builder and replaces current text with it
        text = SpannableStringBuilder.valueOf(text).apply {
            //search for all URL spans and replace all spans with our own clickable spans
            getSpans(0, length, URLSpan::class.java).forEach {
                //add new clickable span at the same position
                setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            onClicked?.invoke(it.url)
                        }
                    },
                    getSpanStart(it),
                    getSpanEnd(it),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                //remove old URLSpan
                removeSpan(it)
            }
        }
        //make sure movement method is set
        movementMethod = LinkMovementMethod.getInstance()
    }
}