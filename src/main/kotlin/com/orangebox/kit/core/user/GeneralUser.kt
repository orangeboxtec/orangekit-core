package com.orangebox.kit.core.user

class GeneralUser {
    val id: String? = null
    val email: String? = null
    val tokenFirebase: String? = null
    val phoneCountryCode: Int? = null
    val phoneNumber: Long? = null
    var name: String? = null
    var lastName: String? = null
    var urlImage: String? = null

    fun toCard(): UserCard{
        val card = UserCard()
        card.id = this.id
        card.name = this.name
        card.lastName = this.lastName
        card.urlImage = this.urlImage
        return card
    }
}