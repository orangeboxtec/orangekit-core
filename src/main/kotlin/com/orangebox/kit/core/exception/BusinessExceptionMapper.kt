package com.orangebox.kit.core.exception

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class BusinessExceptionMapper : ExceptionMapper<BusinessException> {
    override fun toResponse(e: BusinessException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
    }
}