package com.orangebox.kit.core.systemmessage

import com.orangebox.kit.core.dao.AbstractDAO
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SystemMessageDAO: AbstractDAO<SystemMessage>(SystemMessage::class.java) {
    override fun getId(bean: SystemMessage): String? {
        return bean.id
    }
}