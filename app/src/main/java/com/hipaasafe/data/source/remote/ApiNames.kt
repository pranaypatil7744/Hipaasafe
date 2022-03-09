package com.hipaasafe.data.source.remote

class ApiNames {
    companion object{
        const val BASE_URL = "http://3.144.85.173:3500"
        const val BASE_URL_CANDIDATE = "http://google.com/"
        const val PatientSendOtp = "user/patient/login/send-otp"
        const val DoctorLoginSendOtp = "user/login/send-otp"
        const val DoctorLoginValidateOtp = "user/login/validate-otp"
        const val PatientValidateOtp = "user/patient/login/validate-otp"
        const val UserProfile = "user/profile"
        const val PatientRegister = "user/patient/register"
        const val PatientUpdateProfile = "user/patient/update-profile"
        const val GetAppointmentsApi = "query/appointments/fetch/upcoming-past"
        const val ModifyAppointmentsApi = "appointment/patient/modify"
    }
}