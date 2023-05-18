package com.orangebox.kit.core.apptoken

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
        val authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw NotAuthorizedException("Authorization header must be provided")
        }

        // Extract the token from the HTTP Authorization header
        val token = authorizationHeader.substring("Bearer".length).trim { it <= ' ' }
        try {
            // Validate the token
            validateToken(token)
        } catch (e: Exception) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build())
        }
    }

    private fun validateToken(token: String) {
        val validated = appTokenService.checkAppToken(token)
        if (!validated) {
            throw Exception("invalid_token")
        }
    }
}
