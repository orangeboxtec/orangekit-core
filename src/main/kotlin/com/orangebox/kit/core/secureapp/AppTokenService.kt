package com.orangebox.kit.core.secureapp

import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class AppTokenService {

    @Inject
    private lateinit var appTokenDAO: AppTokenDAO

    @ConfigProperty(name = "orangekit.core.secureapp", defaultValue = "false")
    private lateinit var securedApp: String

    fun checkAppToken(token: String?): Boolean {

        if(!securedApp.toBoolean()){
            return true
        }

        val appToken = appTokenDAO.retrieve(
            appTokenDAO.createBuilder()
                .appendParamQuery("token", token!!)
                .build()
        )
        return appToken != null
    }
}