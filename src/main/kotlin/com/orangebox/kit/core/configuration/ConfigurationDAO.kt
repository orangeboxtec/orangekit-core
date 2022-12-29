package com.orangebox.kit.core.configuration

import com.orangebox.kit.core.dao.AbstractDAO
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ConfigurationDAO: AbstractDAO<Configuration>(Configuration::class.java) {
    override fun getId(bean: Configuration): String? {
        return bean.id
    }

    fun loadByCodeNative(code: String?): Configuration? {
        return retrieveByNativeField("key", code)
    }
}