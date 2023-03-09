package com.orangebox.kit.core.user

interface GeneralUser {
    val id: String?
    val email: String?
    val keyIOS: String?
    val keyAndroid: String?
    val phoneCountryCode: Int?
    val phoneNumber: Long?
}