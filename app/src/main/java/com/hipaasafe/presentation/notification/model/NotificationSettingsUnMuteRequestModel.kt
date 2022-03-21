package com.hipaasafe.presentation.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationSettingsUnMuteRequestModel(
    @SerializedName("user-settings")
    var user_settings: UserUnMuteSettings? = UserUnMuteSettings(),
)

data class UserUnMuteSettings(
    var dnd:Boolean? = false,
    var chat: ChatUnMuteModel? = ChatUnMuteModel(),
    var call: CallUnMuteModel? = CallUnMuteModel()
)

data class ChatUnMuteModel(
    var allow_only_mentions:Boolean = false,
    var mute_group_actions:Boolean= false,
    var mute_all_guids:Boolean = false,
    var mute_all_uids:Boolean = false,
    var muted_guids:Boolean = false,
    var muted_uids:Boolean = false
)

data class CallUnMuteModel(
    var mute_all_guids:Boolean = false,
    var mute_all_uids:Boolean = false,
    var muted_guids:Boolean = false,
    var muted_uids:Boolean = false,
)

