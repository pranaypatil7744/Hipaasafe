package com.hipaasafe

class Constants {
    companion object{
        const val VIEW_ANIMATE_DURATION = 400L
        const val IS_LOGIN = "IS_LOGIN"
        const val IS_DOCTOR_LOGIN ="IS_DOCTOR_LOGIN"
        const val LOGIN_WITH ="LOGIN_WITH"
        const val COUNTRY_CODE ="COUNTRY_CODE"
        const val RESEND_OTP_SECOND = 30000.toLong() //i.e 30 secs
        const val ONE_SIGNAL_APP_ID = "29500431-ac41-47af-908c-fcc2ba1d978d"

    }

    class PreferenceKeys{
        companion object{
            const val id ="id"
            const val uid ="uid"
            const val name ="name"
            const val email ="email"
            const val country_code ="country_code"
            const val number ="number"
            const val role_id ="role_id"
            const val role_name ="role_name"
            const val profile_img ="profile_img"
            const val organization_id ="organization_id"
            const val mute_notifications ="mute_notifications"
            const val age ="age"
            const val profile_update ="profile_update"
            const val access_token ="access_token"
            const val location ="location"
            const val experience ="experience"
            const val speciality ="speciality"
            const val tags ="tags"
            const val createdAt ="createdAt"
            const val updatedAt ="updatedAt"
            const val organization_domain ="organization_domain"
        }
    }

    class NotificationType {
        companion object {
            const val FRIEND_REQUEST = "friendRequest"
        }
    }
}