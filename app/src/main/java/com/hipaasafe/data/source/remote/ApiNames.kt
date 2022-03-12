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
        const val UpdateProfilePic = "user/update-profile-pic"
        const val PatientRegister = "user/patient/register"
        const val PatientUpdateProfile = "user/patient/update-profile"
        const val GetAppointmentsApi = "query/appointments/fetch/upcoming-past"
        const val ModifyAppointmentsApi = "appointment/patient/modify"
        const val GetReportsList = "static/hospital-reports/list"
        const val GetMyDoctorsList = "query/patients/fetch/my-doctors"
        const val UploadReportFileApi = "user/test-reports/upload"
        const val UploadAndShareDocumentApi = "documents/user/test-reports/add-new"
        const val GetStaticDetailsApi = "static/common/get"
        const val FetchReportsDocumentListApi = "documents/user/test-reports/fetch"
        const val ShareDocumentApi = "documents/user/test-reports/share"
        const val BookAppointmentApi = "appointment/patient/book"
        const val DoctorAppointmentsListApi = "query/appointments/fetch/by-date"
        const val DoctorMyTeamListApi = "query/doctors/my-team"
    }
}