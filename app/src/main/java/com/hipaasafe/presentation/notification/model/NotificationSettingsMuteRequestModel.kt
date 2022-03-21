package com.hipaasafe.presentation.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationSettingsMuteRequestModel(
    @SerializedName("user-settings")
    var user_settings: UserSettings? = UserSettings(),
)

data class UserSettings(
    var dnd: DndModel? = DndModel(),
    var chat: ChatModel? = ChatModel(),
    var call: CallModel? = CallModel()
)

data class DndModel(
    var dnd: Boolean = true
)
data class ChatModel(
    var allow_only_mentions:Boolean = true,
    var mute_group_actions:Boolean= true,
    var mute_all_guids:Boolean = true,
    var mute_all_uids:Boolean = true,
    var muted_guids:Boolean = true,
    var muted_uids:Boolean = true
)

data class CallModel(
    var mute_all_guids:Boolean = true,
    var mute_all_uids:Boolean = true,
    var muted_guids:Boolean = true,
    var muted_uids:Boolean = true,
)
