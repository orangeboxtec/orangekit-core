package com.orangebox.kit.core.configuration

import com.orangebox.kit.core.dao.AbstractDAO

class ConfigurationDAO: AbstractDAO<Configuration>(Configuration::class.java) {
    override fun getId(bean: Configuration): String? {
        return bean.id
    }
}