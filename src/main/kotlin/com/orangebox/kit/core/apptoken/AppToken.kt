package com.orangebox.kit.core.apptoken

import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId
import java.util.*

@OKEntity(name = "appToken")
class AppToken {
    @OKId
    var id: String? = null
    var token: String? = null
    var name: String? = null
    var creationDate: Date? = null
}
