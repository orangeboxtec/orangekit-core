package com.orangebox.kit.core.systemmessage

import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId

@OKEntity("systemMessage")
class SystemMessage {

    @OKId
    var id: String? = null

    var key: String? = null

    var values: ArrayList<SystemMessageLanguage>? = null
}