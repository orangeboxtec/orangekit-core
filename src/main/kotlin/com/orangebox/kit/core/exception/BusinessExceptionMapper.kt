package com.orangebox.kit.core.exception

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class BusinessExceptionMapper : ExceptionMapper<BusinessException> {
    override fun toResponse(e: BusinessException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
    }
}