package com.hipaasafe.utils.enum


enum class ConversationMode(private val label: String) {
    ALL_CHATS("all_chats"), GROUP("groups"), USER("users");
}

enum class LoginUserType(val value:Int){
    DOCTOR(3),
    PATIENT(5),
    NURSE(4)
}
