package com.orangebox.kit.core.configuration

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ConfigurationService {

    @Inject
    private lateinit var configurationDAO: ConfigurationDAO

    fun list(): List<Configuration?>? {
        return configurationDAO.listAll()
    }

    fun add(configuration: Configuration) {
        configurationDAO.insert(configuration)
    }
}