package com.orangebox.kit.core.user

interface GeneralUser {
    val id: String?
    val email: String?
    val tokenFirebase: String?
    val phoneCountryCode: Int?
    val phoneNumber: Long?
}