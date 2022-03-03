package com.hipaasafe.listener

interface ValidationListener {
    fun onValidationSuccess(type:String,msg:Int)
    fun onValidationFailure(type:String,msg:Int)
}