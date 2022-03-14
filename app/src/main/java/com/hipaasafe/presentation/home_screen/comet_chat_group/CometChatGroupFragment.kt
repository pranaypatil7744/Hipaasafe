package com.hipaasafe.presentation.home_screen.comet_chat_group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.ConversationsRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.helpers.CometChatHelper
import com.cometchat.pro.models.*
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentCometChatGroupBinding
import com.hipaasafe.presentation.home_screen.comet_chat_group.adapter.GroupChatListAdapter
import com.hipaasafe.utils.CometChatUtils
import com.hipaasafe.utils.enum.ConversationMode
import com.hipaasafe.utils.isNetworkAvailable
import java.util.*
import kotlin.collections.ArrayList


class CometChatGroupFragment : BaseFragment(), GroupChatListAdapter.ChatListClickManager {

    lateinit var binding: FragmentCometChatGroupBinding
    lateinit var groupChatListAdapter: GroupChatListAdapter
    var conversationList: ArrayList<Conversation> = ArrayList()
    var originalConversationList: ArrayList<Conversation> = ArrayList()
    var isSearch: Boolean = false
    var isMore:Boolean = true

    companion object {
        private const val TAG = "ConversationList"

        fun newInstance(): CometChatGroupFragment {
            return CometChatGroupFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCometChatGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpListener()
    }

    private fun setUpAdapter() {
        binding.apply {
            groupChatListAdapter = GroupChatListAdapter(
                this@CometChatGroupFragment.requireContext(),
                conversationList,
                this@CometChatGroupFragment
            )
            recyclerChatsList.adapter = groupChatListAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        getChatsList()
        addConversationListener()
    }

    override fun onPause() {
        super.onPause()
        removeConversationListener()
    }

    private fun getChatsList() {
        binding.apply {
            toggleLoader(true)
            val requestBuilder = ConversationsRequest.ConversationsRequestBuilder()
                .setConversationType(ConversationMode.ALL_CHATS.name)
                .setLimit(50)
                .build()
            requestBuilder.fetchNext(object : CometChat.CallbackListener<List<Conversation>>() {
                override fun onSuccess(list: List<Conversation>?) {
                    layoutNoInternet.root.visibility = GONE
                    toggleLoader(false)
                    if (list?.size != 0) {
                        isMore = true
                        layoutNoChat.visibility = INVISIBLE
                        recyclerChatsList.visibility = VISIBLE
                        conversationList.clear()
                        conversationList.addAll(list ?: arrayListOf())
                        originalConversationList.clear()
                        originalConversationList.addAll(conversationList)
                        groupChatListAdapter.notifyDataSetChanged()
                    } else {
                        isMore = false
                        layoutNoChat.visibility = VISIBLE
                        recyclerChatsList.visibility = INVISIBLE
                    }
                }

                override fun onError(e: CometChatException?) {
                    toggleLoader(false)
                    layoutNoChat.visibility = VISIBLE
                    recyclerChatsList.visibility = INVISIBLE
                    if (e?.code == CometChatConstants.Errors.ERROR_API_AUTH_ERR_AUTH_TOKEN_NOT_FOUND || e?.code == "AUTH_ERR_AUTH_TOKEN_NOT_FOUND"){
                        logOutUser(true)
                    }else if (e?.code == "ERROR_USER_NOT_LOGGED_IN"){
                        showToast(getString(R.string.not_a_register_user_please_register_again))
                        logOutUser(false)
                    }else if (e?.code == "ERROR_INTERNET_UNAVAILABLE"){
                        layoutNoInternet.root.visibility = VISIBLE
                        layoutNoChat.visibility = GONE
                    }
                    else{
                        layoutNoInternet.root.visibility = GONE
                        layoutNoChat.visibility = VISIBLE
                        showToast(e?.message.toString())
                    }
                }

            })
        }
    }

    private fun setUpListener() {
        binding.apply {

            recyclerChatsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    if (!recyclerView.canScrollVertically(1)) {
//                        if (!isSearch) {
//                            getChatsList()
//                        }
//                    }
//                }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) { //check for scroll down
                        if (!isSearch) {
                            if (isMore){
                                getChatsList()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun logOutUser(isLogin: Boolean) {
        binding.apply {
            if (requireContext().isNetworkAvailable()){
//                callDeleteUserApi(isLogin)
            }else{
                showToast(getString(R.string.no_internet_connection_please_try_again_later))
            }
        }
    }

    private fun addConversationListener() {
        binding.apply {
            CometChat.addMessageListener(TAG, object : CometChat.MessageListener() {
                override fun onTextMessageReceived(message: TextMessage) {
                    CometChat.markAsDelivered(message)
                    if (groupChatListAdapter != null) {
                        if (message.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                            groupChatListAdapter.refreshConversation(message)
                            checkNoConversation()
                        }
                    }
                }

                override fun onMediaMessageReceived(message: MediaMessage) {
                    CometChat.markAsDelivered(message)
                    if (groupChatListAdapter != null) {
                        if (message.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                            groupChatListAdapter.refreshConversation(message)
                            checkNoConversation()
                        }
                    }
                }

                override fun onCustomMessageReceived(message: CustomMessage) {
                    CometChat.markAsDelivered(message)
                    if (groupChatListAdapter != null) {
                        if (message.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                            groupChatListAdapter.refreshConversation(message)
                            checkNoConversation()
                        }
                    }
                }

                override fun onMessagesDelivered(messageReceipt: MessageReceipt) {
//                    if (rvConversation != null) rvConversation?.setReciept(messageReceipt)
                }

                override fun onMessagesRead(messageReceipt: MessageReceipt) {
//                    if (rvConversation != null) rvConversation?.setReciept(messageReceipt)
                }

                override fun onMessageEdited(message: BaseMessage) {
                    if (groupChatListAdapter != null) {
                        if (message.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                            groupChatListAdapter.refreshConversation(message)
                        }
                    }
                }

                override fun onMessageDeleted(message: BaseMessage) {
                    if (groupChatListAdapter != null) {
                        if (message.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                            groupChatListAdapter.refreshConversation(message)
                        }
                    }
                }
            })
            CometChat.addGroupListener(TAG, object : CometChat.GroupListener() {
                override fun onGroupMemberKicked(
                    action: Action,
                    kickedUser: User,
                    kickedBy: User,
                    kickedFrom: Group
                ) {
                    Log.e(TAG, "onGroupMemberKicked: $kickedUser")
                    if (kickedUser.uid == CometChat.getLoggedInUser().uid) {
                        if (groupChatListAdapter != null) updateConversation(action, true)
                    } else {
                        updateConversation(action, false)
                    }
                }

                override fun onMemberAddedToGroup(
                    action: Action,
                    addedby: User,
                    userAdded: User,
                    addedTo: Group
                ) {
                    Log.e(TAG, "onMemberAddedToGroup: ")
                    updateConversation(action, false)
                }

                override fun onGroupMemberJoined(
                    action: Action,
                    joinedUser: User,
                    joinedGroup: Group
                ) {
                    Log.e(TAG, "onGroupMemberJoined: ")
                    updateConversation(action, false)
                }

                override fun onGroupMemberLeft(action: Action, leftUser: User, leftGroup: Group) {
                    Log.e(TAG, "onGroupMemberLeft: ")
                    if (leftUser.uid == CometChat.getLoggedInUser().uid) {
                        updateConversation(action, true)
                    } else {
                        updateConversation(action, false)
                    }
                }

                override fun onGroupMemberScopeChanged(
                    action: Action,
                    updatedBy: User,
                    updatedUser: User,
                    scopeChangedTo: String,
                    scopeChangedFrom: String,
                    group: Group
                ) {
                    updateConversation(action, false)
                }
            })
        }
    }

    private fun updateConversation(baseMessage: BaseMessage, isRemove: Boolean) {
        if (groupChatListAdapter != null) {
            val conversation = CometChatHelper.getConversationFromMessage(baseMessage)
            if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_GROUP) {
                if (isRemove) groupChatListAdapter.remove(conversation) else groupChatListAdapter.update(
                    conversation
                )
            }
            checkNoConversation()
        }
    }

    private fun removeConversationListener() {
        CometChat.removeMessageListener(TAG)
        CometChat.removeGroupListener(TAG)
    }

    private fun checkNoConversation() {
        binding.apply {
            if (conversationList.size == 0) {
                if (isSearch) {
                    tvNoChatMsg.text = getString(R.string.no_matching_data_found)
                } else {
                    tvNoChatMsg.text =
                        getString(R.string.click_on_second_bottom_tab_and_start_a_conversation_with_doctors)
                }
                layoutNoChat.visibility = VISIBLE
                recyclerChatsList.visibility = GONE
            } else {
                layoutNoChat.visibility = GONE
                recyclerChatsList.visibility = VISIBLE
            }
        }
    }

    override fun clickOnChatGroup(position: Int, group: Group) {
        CometChatUtils.startGroupIntent(group,requireContext())
    }

    override fun clickOnPersonalChat(position: Int, user: User) {
        CometChatUtils.userIntent(user,requireContext())
    }

}