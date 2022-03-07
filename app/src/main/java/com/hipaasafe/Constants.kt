package com.hipaasafe

import com.cometchat.pro.constants.CometChatConstants

class Constants {
    companion object {
        const val VIEW_ANIMATE_DURATION = 400L
        const val IS_LOGIN = "IS_LOGIN"
        const val SUCCESS = "SUCCESS"
        const val IS_DOCTOR_LOGIN = "IS_DOCTOR_LOGIN"
        const val LOGIN_WITH = "LOGIN_WITH"
        const val COUNTRY_CODE = "COUNTRY_CODE"
        const val RESEND_OTP_SECOND = 30000.toLong() //i.e 30 secs
        const val ONE_SIGNAL_APP_ID = "29500431-ac41-47af-908c-fcc2ba1d978d"
        const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val Notifications = "Notifications"
        const val IsImportant = "is_important"
        const val IsForwarded = "is_forwarded"
        const val FIREBASE_TOKEN = "FIREBASE_TOKEN"
        const val NotificationCount = "NotificationCount"


    }

    class ErrorMsg {
        companion object {
            const val NAME_ERROR = "NAME_ERROR"
            const val EMAIL_ERROR = "EMAIL_ERROR"
            const val AGE_ERROR = "AGE_ERROR"
            const val MOBILE_ERROR = "MOBILE_ERROR"
        }
    }

    class PreferenceKeys {
        companion object {
            const val id = "id"
            const val uid = "uid"
            const val name = "name"
            const val email = "email"
            const val country_code = "country_code"
            const val number = "number"
            const val role_id = "role_id"
            const val role_name = "role_name"
            const val avatar = "avatar"
            const val organization_id = "organization_id"
            const val mute_notifications = "mute_notifications"
            const val age = "age"
            const val profile_update = "profile_update"
            const val access_token = "access_token"
            const val location = "location"
            const val experience = "experience"
            const val speciality = "speciality"
            const val tags = "tags"
            const val createdAt = "createdAt"
            const val updatedAt = "updatedAt"
            const val organization_domain = "organization_domain"
        }
    }

    class CometChatConstant {
        companion object {
            const val UID = "uid"
            const val AVATAR = "avatar"
            const val STATUS = "status"
            const val NAME = "name"
            const val TYPE = "type"
            const val GROUP_OWNER = "group_owner"
            const val MEMBER_COUNT = "member_count"
            const val GROUP_MEMBER = "group_members"
            const val GROUP_NAME = "group_name"
            const val GROUP_DESC = "group_description"
            const val GROUP_PASSWORD = "group_password"
            const val GROUP_TYPE = "group_type"
            const val SESSION_ID = "sessionId"
            const val GUID = "guid"
            const val LOCATION = "location"
            const val CUSTOM_MESSAGE = "custom_message"
            const val LOCATION_LATITUDE = "latitude"
            const val LOCATION_LONGITUDE = "longitude"
            const val PARENT_ID = "parent_id"
            const val REPLY_COUNT = "reply_count"
            const val CONVERSATION_NAME = "conversation_name"
            const val STICKERS = "extension_sticker"
            const val REACTION_INFO = "reaction_info"
            const val WHITEBOARD = "extension_whiteboard"
            const val WRITEBOARD = "extension_document"
            const val URL = "url"
            const val MEETING = "meeting"
            const val chat_users_presence_enabled = "core.chat.users.presence.enabled"
            const val chat_messages_receipts_enabled = "core.chat.messages.receipts.enabled"
        }

        object MapUrl {
            const val MAPS_URL =
                "https://maps.googleapis.com/maps/api/staticmap?zoom=16&size=380x220&markers=color:red|"
            var MAP_ACCESS_KEY = "AIzaSyAa8HeLH2lQMbPeOiMlM9D1VxZ7pbGQq8o"
        }

        object MessageRequest {

            var messageTypesForUser: MutableList<String> = ArrayList(
                listOf(
                    CometChatConstants.MESSAGE_TYPE_CUSTOM,
                    CometChatConstants.MESSAGE_TYPE_AUDIO,
                    CometChatConstants.MESSAGE_TYPE_TEXT,
                    CometChatConstants.MESSAGE_TYPE_IMAGE,
                    CometChatConstants.MESSAGE_TYPE_VIDEO,
                    CometChatConstants.MESSAGE_TYPE_FILE,
                    //Custom Messages
                    LOCATION,
                    STICKERS
                )
            )
            var messageTypesForGroup: MutableList<String> = ArrayList(
                listOf(
                    CometChatConstants.MESSAGE_TYPE_CUSTOM,
                    CometChatConstants.MESSAGE_TYPE_AUDIO,
                    CometChatConstants.MESSAGE_TYPE_TEXT,
                    CometChatConstants.MESSAGE_TYPE_IMAGE,
                    CometChatConstants.MESSAGE_TYPE_VIDEO,
                    CometChatConstants.MESSAGE_TYPE_FILE,
                    //For Group Actions
                    CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER,
                    LOCATION,
                    STICKERS,
                    MEETING
                )
            )

            var messageCategoriesForGroup: MutableList<String> = ArrayList(
                listOf(
                    CometChatConstants.CATEGORY_MESSAGE,
                    CometChatConstants.CATEGORY_CUSTOM,
                    CometChatConstants.CATEGORY_CALL,
                    CometChatConstants.CATEGORY_ACTION
                )
            )


            var messageCategoriesForUser: MutableList<String> = ArrayList(
                listOf(
                    CometChatConstants.CATEGORY_MESSAGE,
                    CometChatConstants.CATEGORY_CUSTOM,
                    CometChatConstants.CATEGORY_CALL
                )
            )
        }
    }

    class NotificationType {
        companion object {
            const val FRIEND_REQUEST = "friendRequest"
        }
    }
}