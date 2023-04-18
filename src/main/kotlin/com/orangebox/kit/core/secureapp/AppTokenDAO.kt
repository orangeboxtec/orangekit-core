package com.orangebox.kit.core.secureapp

import com.orangebox.kit.core.dao.AbstractDAO

class AppTokenDAO : AbstractDAO<AppToken>(AppToken::class.java) {
    override fun getId(bean: AppToken): Any? {
        return bean.id
    }
}