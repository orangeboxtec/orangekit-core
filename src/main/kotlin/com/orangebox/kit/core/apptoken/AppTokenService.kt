package com.orangebox.kit.core.apptoken

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class AppTokenService {

    @Inject
    private lateinit var appTokenDAO: AppTokenDAO

    @ConfigProperty(name = "orangekit.core.applicationtoken", defaultValue = "false")
    private lateinit var appToken: String

    fun checkAppToken(token: String): Boolean {
        println("VERIFICANDO TOKEN NA BASE: $token")
        if(appToken.toBoolean()){
            val appToken = appTokenDAO.retrieve(appTokenDAO.createBuilder()
                .appendParamQuery("token", token)
                .build()
            )
            return appToken != null
        }
        return true
    }
}
