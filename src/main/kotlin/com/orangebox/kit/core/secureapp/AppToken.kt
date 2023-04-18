package com.orangebox.kit.core.secureapp

import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId
import java.util.*
import javax.json.bind.annotation.JsonbDateFormat

@OKEntity(name = "appToken")
class AppToken {
    @OKId
    var id: String? = null
    var token: String? = null
    var name: String? = null

    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    var creationDate: Date? = null
}