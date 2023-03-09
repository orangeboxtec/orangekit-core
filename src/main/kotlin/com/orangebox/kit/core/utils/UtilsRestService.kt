package com.orangebox.kit.core.utils

import com.fasterxml.jackson.databind.ObjectMapper
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Stateless
@Path("/utils")
class UtilsRestService {
    @EJB
    private val typeService: TypeService? = null
    @GET
    @Path("/listTypes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listTypes(): String? {
        var resultStr: String
        try {
            val list = typeService!!.listTypes()
            val mapper = ObjectMapper()
            resultStr = mapper.writeValueAsString(list)
        } catch (e: Exception) {
            e.printStackTrace()
            resultStr = e.message
        }
        return resultStr
    }
}