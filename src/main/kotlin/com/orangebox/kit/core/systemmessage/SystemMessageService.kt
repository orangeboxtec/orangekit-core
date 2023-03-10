package com.orangebox.kit.core.systemmessage

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SystemMessageService {

    @Inject
    private lateinit var systemMessageDAO: SystemMessageDAO

    fun list(): List<SystemMessage>? {
        return systemMessageDAO.listAll()
    }

    fun save(systemMessage: SystemMessage) {
        if(systemMessage.id == null){
            systemMessageDAO.insert(systemMessage)
        }
        else {
            systemMessageDAO.update(systemMessage)
        }
    }

    fun loadByKey(key: String): SystemMessage? {
        return systemMessageDAO.retrieve(
            systemMessageDAO.createBuilder()
                .appendParamQuery("key", key)
                .build()
        )
    }

    fun checkAndSave(key: String, language: String, message: String) {
        var systemMessage = systemMessageDAO.retrieve(systemMessageDAO.createBuilder()
            .appendParamQuery("key", key)
            .appendParamQuery("values.language", language)
            .build())
        if (systemMessage == null) {

            systemMessage = systemMessageDAO.retrieve(systemMessageDAO.createBuilder()
                .appendParamQuery("key", key)
                .build())

            val message = SystemMessageLanguage().apply {
                this.language = language
                this.message = message
            }

            if (systemMessage == null) {
                systemMessage = SystemMessage()
                systemMessage.key = key
                systemMessage.values = ArrayList()
                systemMessage.values!!.add(message)
                systemMessageDAO.insert(systemMessage)
            } else {
                systemMessage.values!!.add(message)
                systemMessageDAO.update(systemMessage)
            }
        }
    }
}