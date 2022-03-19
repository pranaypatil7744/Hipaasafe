package com.hipaasafe

import com.cometchat.pro.constants.CometChatConstants

class Constants {
    companion object {
        const val ONE_SIGNAL_APP_ID = "29500431-ac41-47af-908c-fcc2ba1d978d"
        const val GENIUS_SDK_LICENSE = "533c5006575e0906045d075539525a0e4a0b5b145557415804536b57010b08095a0d57560b"

        const val AUTHORITY = "com.hipaasafe.fileprovider"
        const val BASE_URL_PROFILE_PIC = "https://akbh.s3.ap-south-1.amazonaws.com/hs/avatar/"
        const val BASE_URL_REPORT= "https://akbh.s3.ap-south-1.amazonaws.com/hs/user/reports/"
        const val VIEW_ANIMATE_DURATION = 400L
        const val IS_LOGIN = "IS_LOGIN"
        const val MOBILE = "MOBILE"
        const val SUCCESS = "SUCCESS"
        const val DETECTED_TEXT = "DETECTED_TEXT"
        const val QR_MODEL = "QR_MODEL"
        const val SCAN_MODEL = "SCAN_MODEL"
        const val IS_DOCTOR_LOGIN = "IS_DOCTOR_LOGIN"
        const val LOGIN_WITH = "LOGIN_WITH"
        const val COUNTRY_CODE = "COUNTRY_CODE"
        const val RESEND_OTP_SECOND = 30000.toLong() //i.e 30 secs
        const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val Notifications = "Notifications"
        const val IsImportant = "is_important"
        const val IsForwarded = "is_forwarded"
        const val IS_FROM_GROUP_CHAT = "IS_FROM_GROUP_CHAT"
        const val PAST = "PAST"
        const val UPCOMING = "UPCOMING"
        const val CONFIRM = "CONFIRM"
        const val CONFIRMED = "CONFIRMED"
        const val CANCEL = "CANCEL"
        const val CANCELLED = "CANCELLED"
        const val RESCHEDULE = "RESCHEDULE"
        const val PENDING = "PENDING"
        const val RESCHEDULED = "RESCHEDULED"
        const val COMPLETED = "COMPLETED"
        const val REMINDER = "REMINDER"
        const val NEXT_IN_Q = "NEXT_IN_Q"

        const val IsFromAdd= "IsFromAdd"
        const val IsForAttachDoc= "IsForAttachDoc"
        const val AttachmentSendTo= "AttachmentSendTo"
        const val IS_PDF = "IS_PDF"
        const val IS_SELFIE_CAMERA = "IS_SELFIE_CAMERA"
        const val IS_CAMERA = "IS_CAMERA"
        const val FIREBASE_TOKEN = "FIREBASE_TOKEN"
        const val NotificationCount = "NotificationCount"
        const val PendingDocumentName = "PendingDocumentName"
        const val PendingDocumentType = "PendingDocumentType"
        const val PendingDocumentId = "PendingDocumentId"
        const val DocumentRequestId = "DocumentRequestId"
        const val PendingDocumentBy = "PendingDocumentBy"
        const val PendingDocumentGuid = "PendingDocumentGuid"
        const val PendingDocumentDoctorId = "PendingDocumentDoctorId"
        const val DoctorsList = "DoctorsList"
        const val DocumentLink = "DocumentLink"


    }
    class AskedPermission {
        companion object {
            const val IS_ASKED_CAMERA_PERMISSION = "IS_ASKED_CAMERA_PERMISSION"
            const val CAMERA_PERMISSION_COUNT = "CAMERA_PERMISSION_COUNT"
            const val IS_ASKED_STORAGE_PERMISSION = "IS_ASKED_STORAGE_PERMISSION"
            const val STORAGE_PERMISSION_COUNT = "STORAGE_PERMISSION_COUNT"
            const val IS_ASKED_LOCATION_PERMISSION = "IS_ASKED_LOCATION_PERMISSION"
            const val LOCATION_PERMISSION_COUNT = "LOCATION_PERMISSION_COUNT"
            const val IS_ASKED_CALL_PERMISSION = "IS_ASKED_CALL_PERMISSION"
            const val CALL_PERMISSION_COUNT = "CALL_PERMISSION_COUNT"
            const val IS_CONTACT_PERMISSION_ASKED = "IS_CONTACT_PERMISSION_ASKED"
            const val IS_PROFILE_PERMISSION_ASKED = "IS_PROFILE_PERMISSION_ASKED"
            const val IS_LOCATION_PERMISSION_ASKED = "IS_LOCATION_PERMISSION_ASKED"
            const val IS_STORAGE_PERMISSION_ASKED = "IS_STORAGE_PERMISSION_ASKED"
        }
    }

    class PermissionRequestCodes {
        companion object {
            const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 11000
            const val REQUEST_CODE_CHECK_SETTINGS = 1000
            const val STORAGE_PERMISSION_CODE = 100
            const val CAMERA_PERMISSION_CODE = 101
            const val CALL_PHONE_PERMISSION_CODE = 102
        }
    }

    class ErrorMsg {
        companion object {
            const val NAME_ERROR = "NAME_ERROR"
            const val EMAIL_ERROR = "EMAIL_ERROR"
            const val AGE_ERROR = "AGE_ERROR"
            const val MOBILE_ERROR = "MOBILE_ERROR"
            const val DOB_ERROR = "DOB_ERROR"
        }
    }

    class IntentExtras {
        companion object {
            const val EXTRA_LATITUDE = "latitude"
            const val EXTRA_LONGITUDE = "longitude"
            const val EXTRA_PICKUP_ADDRESS = "pickup_address"
            const val EXTRA_FILE_NAME = "EXTRA_FILE_NAME"
            const val EXTRA_FILE_PATH = "EXTRA_FILE_PATH"
            const val EXTRA_IMAGE_BIT_MAP = "EXTRA_IMAGE_BIT_MAP"
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
            const val dob = "dob"
            const val profile_update = "profile_update"
            const val access_token = "access_token"
            const val location = "location"
            const val experience = "experience"
            const val qr_code = "qr_code"
            const val speciality = "speciality"
            const val specialityModel = "specialityModel"
            const val doctorsMappedModel = "doctorsMappedModel"
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
            const val MESSAGE_CATEGORY = "message_category"
            const val FILE_NAME = "file_name"
            const val FILE_URL = "file_url"
            const val FILE_MIME_TYPE = "file_mime"
            const val FILE_EXTENSION = "file_extension"
            const val FILE_SIZE = "file_size"
            const val speciality = "speciality"
            const val experience = "experience"
            const val GROUP_OWNER = "group_owner"
            const val MEMBER_COUNT = "member_count"
            const val GROUP_MEMBER = "group_members"
            const val GROUP_NAME = "group_name"
            const val GROUP_DESC = "group_description"
            const val GROUP_PASSWORD = "group_password"
            const val GROUP_TYPE = "group_type"
            const val SESSION_ID = "sessionId"
            const val GUID = "guid"
            const val JOIN_ONGOING = "join_ongoing_call"
            const val ID = "id"
            const val INCOMING = "incoming"

            const val LOCATION = "location"
            const val CUSTOM_MESSAGE = "custom_message"
            const val LOCATION_LATITUDE = "latitude"
            const val LOCATION_LONGITUDE = "longitude"
            const val PARENT_ID = "parent_id"
            const val PATIENT_ID = "PATIENT_ID"
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
            val EXTRA_MIME_DOC = arrayOf("text/plane", "text/html", "application/pdf", "application/msword","application/vnd.ms-word.document.macroEnabled.12",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                , "application/vnd.ms.excel","application/vnd.ms-excel.sheet.macroEnabled.12","application/vnd.ms-excel.sheet.binary.macroEnabled.12",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","application/mspowerpoint","application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation","application/zip")
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