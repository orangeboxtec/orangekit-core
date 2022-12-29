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

    fun loadByCode(code: String): Configuration? {
        return configurationDAO.retrieve(
            configurationDAO.createBuilder()
                .appendParamQuery("key", code)
                .build()
        )
    }

    fun saveConfiguration(configuration: Configuration) {
        val configurationBase = loadByCode(configuration.key!!)
        if (configurationBase != null) {
            configurationBase.value = configuration.value!!
            configurationDAO.update(configurationBase)
        } else {
            configurationDAO.insert(configuration)
        }
    }

    fun insertConfiguration(configuration: Configuration) {
        configurationDAO.insert(configuration)
    }

    @Throws(Exception::class)
    fun checkAndSave(key: String, value: String) {
        var configuration = configurationDAO.loadByCodeNative(key)
        if (configuration == null) {
            configuration = Configuration()
            configuration.key = key
            configuration.value = value
            insertConfiguration(configuration)
        }
    }

    fun checkAndSave(key: String, value: Map<String, String>) {
        var configuration = configurationDAO.loadByCodeNative(key)
        if (configuration == null) {
            configuration = Configuration()
            configuration.key = key
            configuration.valueData = value
            insertConfiguration(configuration)
        }
    }

    fun loadMobileConfiguration(): List<Configuration?>? {
        return configurationDAO.search(
            configurationDAO.createBuilder()
                .appendParamQuery("fgMobile", true)
                .build()
        )
    }
}