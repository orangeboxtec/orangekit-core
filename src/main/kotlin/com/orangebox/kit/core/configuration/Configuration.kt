package com.orangebox.kit.core.configuration

class Configuration {

    var id: String? = null

    var key: String? = null

    var value: String? = null

    var valueData: Map<String, String>? = null

    var fgActive: Boolean? = null

    var fgMobile: Boolean? = null

    constructor()

    constructor(id: String?) {
        this.id = id
    }

    constructor(key: String?, value: String?) {
        this.key = key
        this.value = value
    }
}