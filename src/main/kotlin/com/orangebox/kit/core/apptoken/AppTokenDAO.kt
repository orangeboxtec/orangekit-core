package com.orangebox.kit.core.apptoken

import com.orangebox.kit.core.dao.AbstractDAO
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AppTokenDAO : AbstractDAO<AppToken>(AppToken::class.java) {
    override fun getId(bean: AppToken): Any {
        return bean.id!!
    }
}
