package com.oyasis.diabetescare.data.models

data class Status(
    var timePosted: String,
    var issueText: String
) {
    constructor():this("","")
}
