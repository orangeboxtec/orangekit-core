package com.orangebox.kit.core.apptoken

import com.orangebox.kit.core.dao.AbstractDAO

class AppTokenDAO : AbstractDAO<AppToken>(AppToken::class.java) {
    override fun getId(obj: AppToken): Any {
        return obj.id!!
    }
}
