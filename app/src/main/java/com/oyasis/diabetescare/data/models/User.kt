package com.oyasis.diabetescare.data.models

import android.content.Context
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var UserId: String,
    var EmailAddress: String,
    var FirstName: String,
    var LastName: String,
    var Role: String,
    var Telephone: String,
    var DiabetesStage: String?,
    var PatientNo: String?,
    var DoctorId: String?,
) {
    // Default Constructor
    constructor() : this("","","","","","","","","")

    // Helpers
    companion object {
        public const val USER_ID = "UserID";
        public const val EMAIL_ADDR = "EmailAddress"
        public const val FIRST_NAME = "FirstName"
        const val LAST_NAME = "LastName"
        const val ROLE = "Role"
        const val TELEPHONE = "Telephone"
        public const val D_STAGE = "DStage"
        public const val PATIENT_NO = "PatientNo"
        public const val DOCTOR_ID = "Doctor"

        const val DOC_NAME = "user_details"

        fun fromPersistance(ctx: Context): User {
            val sharedPreferences = ctx.getSharedPreferences(DOC_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getString(USER_ID, "")!!,
                sharedPreferences.getString(EMAIL_ADDR, "")!!,
                sharedPreferences.getString(FIRST_NAME, "")!!,
                sharedPreferences.getString(LAST_NAME, "")!!,
                sharedPreferences.getString(ROLE, "")!!,
                sharedPreferences.getString(TELEPHONE, "")!!,
                sharedPreferences.getString(D_STAGE, null),
                sharedPreferences.getString(PATIENT_NO, null),
                sharedPreferences.getString(DOCTOR_ID, null)
            )
        }

        fun logOut(ctx: Context) {
            val sharedPreferences = ctx.getSharedPreferences(User.DOC_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString(User.USER_ID, null)
            editor.putString(User.EMAIL_ADDR, null)
            editor.putString(User.FIRST_NAME, null)
            editor.putString(User.LAST_NAME, null)
            editor.putString(User.ROLE, null)
            editor.putString(User.TELEPHONE, null)
            editor.putString(User.D_STAGE, null)
            editor.putString(User.PATIENT_NO, null)
            editor.putString(User.DOCTOR_ID, null)

            editor.apply()
        }
    }
}


fun User.saveToPersistance(ctx: Context) {
    val sharedPreferences = ctx.getSharedPreferences(User.DOC_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(User.USER_ID, UserId)
    editor.putString(User.EMAIL_ADDR, EmailAddress)
    editor.putString(User.FIRST_NAME, FirstName)
    editor.putString(User.LAST_NAME, LastName)
    editor.putString(User.ROLE, Role)
    editor.putString(User.TELEPHONE, Telephone)
    editor.putString(User.D_STAGE, DiabetesStage)
    editor.putString(User.PATIENT_NO, PatientNo)
    editor.putString(User.DOCTOR_ID, DoctorId)
    editor.apply()
}


