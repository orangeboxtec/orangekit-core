package com.orangebox.kit.core.configuration

import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId

@OKEntity("configuration")
class Configuration {

    @OKId
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
}