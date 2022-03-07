package com.hipaasafe.settings

import android.content.Context
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.hipaasafe.Constants

class CometChatSettings(context: Context) {

    public var context: Context = context

    companion object {


        //style
        var color = "#03A9F4"

        //BottomBarcalls
        var showUsersBB = true
        var showGroupsBB = true
        var showChatsBB = true
        var showCallsBB = true
        var showUserSettingsBB = true
        var groupListing = "public_and_password_protected_groups"
        var userListing = "all_users"

        //main
        var sendMessageInOneOnOne = true
        var sendMessageInGroup = true
        var publicGroup: Boolean = true
        var privateGroup: Boolean = true
        var passwordGroup: Boolean = true
        var users: Boolean = true
        var calls: Boolean = true
        var groups: Boolean = true
        var conversations: Boolean = true
        var userSettings: Boolean = true
        var showUserPresence = true
        var sendTypingIndicator: Boolean = true
        var showReadDeliveryReceipts = true
        var replyingToMessage: Boolean = true
        var threadedChats: Boolean = true
        var sendPhotoVideos: Boolean = true
        var sendFiles = true
        var sendVoiceNotes = true
        var messageHistory: Boolean = true
        var unreadCount: Boolean = true
        var searchUsers: Boolean = true
        var searchGroups: Boolean = true
        var searchMessages: Boolean = true

        // Voice & Video Calling/Conferencing
        var userAudioCall: Boolean = true
        var userVideoCall: Boolean = true
        var groupAudioCall: Boolean = true
        var groupVideoCall: Boolean = true
        var callRecording: Boolean = true
        var callLiveStreaming: Boolean = true
        var callTranscription: Boolean = true

        //User Experience
        var thumbnailGeneration: Boolean = true
        var linkPreview: Boolean = true
        var saveMessages: Boolean = true
        var pinMessages: Boolean = true
        var richMediaPreview: Boolean = true
        var voiceTranscription: Boolean = true

        //User Engagement
        var sendEmojis = true
        var mentions: Boolean = true
        var sendMessageReaction: Boolean = true
        var sendLiveReaction: Boolean = true
        var messageTranslation: Boolean = true
        var smartReplies: Boolean = true
        var polls: Boolean = true
        var collaborativeWhiteboard: Boolean = true
        var collaborativeDocument: Boolean = true
        var blockUser = true
        var allowModeratorToDeleteMemberMessages: Boolean = true
        var setGroupInQnaModeByModerators: Boolean = true
        var highlightMessageFromModerators: Boolean = true
        var kickMember: Boolean = true
        var banMember: Boolean = true
        var profanityFilter: Boolean = true
        var imageModeration: Boolean = true
        var dataMasking: Boolean = true
        var malwareScanner: Boolean = true
        var sentimentAnalysis: Boolean = true
        var inflightMessageModeration: Boolean = true
        var messageInPrivate: Boolean = true
        var viewProfile: Boolean = true
        var searchChats: Boolean = true


        var deleteMessage = true
        var shareCopyForwardMessage = true
        var sendEmojisInLargerSize = true
        var editMessage = true
        var shareLocation = true

        var allowDeleteGroup = true
        var enableSoundForMessages = true
        var enableSoundForCalls = true
        var joinOrLeaveGroup = true
        var sendGifs: Boolean = true
        var viewShareMedia = true
        var viewGroupMembers = true
        var callNotifications = true
        var allowPromoteDemoteMembers = true
        var allowAddMembers = true
        var joinLeaveNotifications = true
        var groupCreation = true
        var emailReplies = true
        var hideDeletedMessages = true
        var chatListMode = true
        var sendStickers = true
        var showReplyPrivately = false

        var deleteConversation = true

        fun sendMessageInOneOnOne(sendMessageInOneOnOne: Boolean) {
            this.sendMessageInOneOnOne = sendMessageInOneOnOne
        }

        fun sendMessageInGroup(sendMessageInGroup: Boolean) {
            this.sendMessageInGroup = sendMessageInGroup
        }

//        fun setColor(color: String) {
//            this.color = color
//        }

        fun setMessagingSoundEnable(isEnable: Boolean) {
            this.enableSoundForMessages = isEnable
        }


        fun setCallSoundEnable(isEnable: Boolean) {
            this.enableSoundForCalls = isEnable
        }

        fun enableLiveReaction(isEnable: Boolean) {
            this.sendLiveReaction = isEnable
        }


        fun showReplyPrivately(isVisible: Boolean) {
            this.showReplyPrivately = isVisible
        }

        @Deprecated("")
        fun showCallNotification(isVisible: Boolean) {
            if (!isVisible) {
                Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup
                    .remove(CometChatConstants.CATEGORY_CALL)
                Constants.CometChatConstant.MessageRequest.messageTypesForUser
                    .remove(CometChatConstants.CATEGORY_CALL)
            } else {
                if (!Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup
                        .contains(CometChatConstants.CATEGORY_CALL)
                ) {
                    Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup
                        .add(CometChatConstants.CATEGORY_CALL)
                }
                if (!Constants.CometChatConstant.MessageRequest.messageCategoriesForUser
                        .contains(CometChatConstants.CATEGORY_CALL)
                ) {
                    Constants.CometChatConstant.MessageRequest.messageCategoriesForUser
                        .add(CometChatConstants.CATEGORY_CALL)
                }
            }
        }

        fun hideCallActions(isHidden: Boolean) {
            this.callNotifications = isHidden
            if (isHidden) {
                Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup
                    .remove(CometChatConstants.CATEGORY_CALL)
                Constants.CometChatConstant.MessageRequest.messageTypesForUser
                    .remove(CometChatConstants.CATEGORY_CALL)
            } else {
                if (!Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup
                        .contains(CometChatConstants.CATEGORY_CALL)
                ) {
                    Constants.CometChatConstant.MessageRequest.messageCategoriesForGroup
                        .add(CometChatConstants.CATEGORY_CALL)
                }
                if (!Constants.CometChatConstant.MessageRequest.messageCategoriesForUser
                        .contains(CometChatConstants.CATEGORY_CALL)
                ) {
                    Constants.CometChatConstant.MessageRequest.messageCategoriesForUser
                        .add(CometChatConstants.CATEGORY_CALL)
                }
            }
        }

        @Deprecated("")
        fun showGroupNotification(isVisible: Boolean) {
            if (!isVisible) {
                Constants.CometChatConstant.MessageRequest.messageTypesForGroup
                    .remove(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)
            } else {
                if (!Constants.CometChatConstant.MessageRequest.messageTypesForGroup
                        .contains(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)
                ) {
                    Constants.CometChatConstant.MessageRequest.messageTypesForGroup
                        .add(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)
                }
            }
        }

        fun hideGroupActions(isHidden: Boolean) {
            this.joinLeaveNotifications = isHidden
            if (isHidden) {
                Constants.CometChatConstant.MessageRequest.messageTypesForGroup
                    .remove(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)
            } else {
                if (!Constants.CometChatConstant.MessageRequest.messageTypesForGroup
                        .contains(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)
                ) {
                    Constants.CometChatConstant.MessageRequest.messageTypesForGroup
                        .add(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)
                }
            }
        }

        fun showUsersInNavigation(showUsers: Boolean) {
            this.showUsersBB = showUsers
        }

        fun showGroupsInNavigation(showGroups: Boolean) {
            this.showGroupsBB = showGroups
        }

        fun showChatsInNavigation(showChats: Boolean) {
            this.showChatsBB = showChats
        }

        fun showCallsInNavigation(showCalls: Boolean) {
            this.showCallsBB = showCalls
        }

        fun showSettingsInNavigation(showUserSettings: Boolean) {
            this.showUserSettingsBB = showUserSettings
        }

        fun setGroupType(groupListing: String) {
            this.groupListing = groupListing
        }

        fun setUsersType(userListing: String) {
            this.userListing = userListing
        }

        fun showReadDeliveryReceipts(showReadDeliveryReceipts: Boolean) {
            this.showReadDeliveryReceipts = showReadDeliveryReceipts
        }

        fun allowEmojisInLargeSize(sendEmojisLargeSize: Boolean) {
            this.sendEmojisInLargerSize = sendEmojisLargeSize
        }

        fun allowSendingEmojis(sendEmojis: Boolean) {
            this.sendEmojis = sendEmojis
        }


        fun allowSendingVoiceNotes(sendVoiceNotes: Boolean) {
            this.sendVoiceNotes = sendVoiceNotes
        }


        fun allowSendingFiles(sendFiles: Boolean) {
            this.sendFiles = sendFiles
        }

        fun allowSendingPolls(sendPolls: Boolean) {
            this.polls = sendPolls
        }

        fun allowSendingPhotosVideo(sendPhotosVideo: Boolean) {
            this.sendPhotoVideos = sendPhotosVideo
        }

        fun enableThreadedReplies(enableThreadedReplies: Boolean) {
            this.threadedChats = enableThreadedReplies
        }

        fun enableReplyToMessage(enableReplyToMessage: Boolean) {
            this.replyingToMessage = enableReplyToMessage
        }

//        fun setEnableShareCopyForward(enableShareCopyForward: Boolean) {
//            this.enableShareCopyForward = enableShareCopyForward
//        }

        fun allowDeletingMessage(enableDeleteMessage: Boolean) {
            this.deleteMessage = enableDeleteMessage
        }

        fun allowEditingMessage(enableEditingMessage: Boolean) {
            this.editMessage = enableEditingMessage
        }

        fun allowShareLocation(shareLocation: Boolean) {
            this.shareLocation = shareLocation
        }

        fun allowUsersToBlock(allowUserToblockUser: Boolean) {
            this.blockUser = allowUserToblockUser
        }

        fun showTypingIndicators(showTypingIndicators: Boolean) {
            this.sendTypingIndicator = showTypingIndicators
        }

        fun showSharedMedia(showSharedMedia: Boolean) {
            this.viewShareMedia = showSharedMedia
        }

//        fun setShowUserPresence(showUserPresence: Boolean) {
//            this.showUserPresence = showUserPresence
//        }

        fun allowPromoteDemoteMembers(allowPromoteDemoteMembers: Boolean) {
            this.allowPromoteDemoteMembers = allowPromoteDemoteMembers
        }

        fun allowBanMembers(allowBanKickMembers: Boolean) {
            this.banMember = allowBanKickMembers
        }

        fun allowKickMembers(kickMember: Boolean) {
            this.kickMember = kickMember
        }

        fun allowAddMembersInGroup(allowAddMembersInGroup: Boolean) {
            this.allowAddMembers = allowAddMembersInGroup
        }

        fun allowModeratorToDeleteMessages(allowModeratorToDeleteMessages: Boolean) {
            this.allowModeratorToDeleteMemberMessages = allowModeratorToDeleteMessages
        }

        fun allowDeleteGroups(allowDeleteGroups: Boolean) {
            this.allowDeleteGroup = allowDeleteGroups
        }

        fun showGroupMembers(showGroupMembers: Boolean) {
            this.viewGroupMembers = showGroupMembers
        }

        fun allowJoinOrLeaveGroup(joinOrLeaveGroup: Boolean) {
            this.joinOrLeaveGroup = joinOrLeaveGroup
        }


        fun showGroupCreate(groupCreate: Boolean) {
            this.groupCreation = groupCreate
        }

        fun enableGroupVideoCall(groupVideoCall: Boolean) {
            this.groupVideoCall = groupVideoCall
        }

        fun enableUserVideoCall(enableUserVideoCall: Boolean) {
            this.userVideoCall = enableUserVideoCall
        }

        fun enableUserVoiceCalling(userAudioCall: Boolean) {
            this.userAudioCall = userAudioCall
        }

        fun enableReactionsOnMessage(enableReactionOnMessage: Boolean) {
            this.sendMessageReaction = enableReactionOnMessage
        }

        fun enableCollaborativeWhiteBoard(enableWhiteBoardSharing: Boolean) {
            this.collaborativeWhiteboard = enableWhiteBoardSharing
        }

        fun enableCollaborativeWriteBoard(enableWriteBoardSharing: Boolean) {
            this.collaborativeDocument = enableWriteBoardSharing
        }

        fun enableMessageTranslation(enableMessageTranslation: Boolean) {
            this.messageTranslation = enableMessageTranslation
        }


    }

    fun addConnectionListener(tag: String) {
        CometChat.addConnectionListener(tag, object : CometChat.ConnectionListener {
            override fun onConnected() {
//                Toast.makeText(context, "OnConnected", Toast.LENGTH_LONG).show()
            }

            override fun onConnecting() {
//                Toast.makeText(context, "OnConnecting", Toast.LENGTH_LONG).show()
            }

            override fun onDisconnected() {
//                Toast.makeText(context, "OnDisConnected", Toast.LENGTH_LONG).show()
            }

            override fun onFeatureThrottled() {
//                Toast.makeText(context, "OnFeatureThrottled", Toast.LENGTH_LONG).show()
            }

        })
    }


}