package com.orangebox.kit.core.user

interface GeneralUser {
    val id: String?
    val email: String?
    val tokenFirebase: String?
    val phoneCountryCode: Int?
    val phoneNumber: Long?
    val name: String?
    val lastName: String?
    val urlImage: String?

    fun toCard(): UserCard{
        val card = UserCard()
        card.id = this.id
        card.name = this.name
        card.lastName = this.lastName
        card.urlImage = this.urlImage
        card.email = this.email
        return card
    }
}