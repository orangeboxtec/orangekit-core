package com.orangebox.kit.core.secureapp

import javax.annotation.Priority
import javax.inject.Inject
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.Priorities
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

@SecuredApp
@Provider
@Priority(Priorities.AUTHENTICATION)
class AuthenticationFilter : ContainerRequestFilter {


    @Inject
    private lateinit var appTokenService: AppTokenService


    override fun filter(requestContext: ContainerRequestContext) {

        var validate = false
//        try {
//            val appTokenRequired: Configuration = configurationService.loadByCode("APPTOKEN_REQUIRED")
//            if (appTokenRequired != null && appTokenRequired.getValueAsBoolean()) {
//                validate = true
//            }
//        } catch (ignored: Exception) {
//        }

        if (validate) {
            // Get the HTTP Authorization header from the request
            val authorizationHeader: String = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)

            // Check if the HTTP Authorization header is present and formatted correctly
            if (!authorizationHeader.startsWith("Bearer ")) {
                throw NotAuthorizedException("Authorization header must be provided")
            }

            // Extract the token from the HTTP Authorization header
            val token = authorizationHeader.substring("Bearer".length).trim { it <= ' ' }
            try {
                validateToken(token)
            } catch (e: Exception) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build())
                if (e.message!!.isNotEmpty()) {
                    throw NotAuthorizedException(e.message)
                } else {
                    throw NotAuthorizedException("Error provided Token")
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun validateToken(token: String) {
        val validated = appTokenService!!.checkAppToken(token)
        if (!validated) {
            throw Exception("Invalid Token")
        }
    }
}