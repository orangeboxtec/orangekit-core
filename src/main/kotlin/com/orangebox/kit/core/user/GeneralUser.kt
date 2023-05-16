package com.orangebox.kit.core.user

interface GeneralUser {
    val id: String?
    val email: String?
    val tokenFirebase: String?
    val phoneCountryCode: Int?
    val phoneNumber: Long?
    var name: String?
    var lastName: String?
    var urlImage: String?

    fun toCard(): UserCard{
        val card = UserCard()
        card.id = this.id
        card.name = this.name
        card.lastName = this.lastName
        card.urlImage = this.urlImage
        return card
    }
}