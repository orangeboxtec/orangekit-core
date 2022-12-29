package com.orangebox.kit.core.configuration

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ConfigurationService {

    @Inject
    private lateinit var configurationDAO: ConfigurationDAO

    fun list(): List<Configuration>? {
        return configurationDAO.search(configurationDAO.createBuilder()
            .appendParamQuery("key", "chave")
            .build())
    }

    fun add(configuration: Configuration) {
        configurationDAO.insert(configuration)
    }
}