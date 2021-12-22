package com.oyasis.diabetescare.data.models

data class MedicalAdvice(
    var advice: String,
    var timeStampPosted: Long,
    var appointmentDate: String?,
    var appointmentTime: String?,
    var doctorName: String,
    var appointmentAddress: String?
) {
    constructor(): this("",0,"","", "", null)
}
