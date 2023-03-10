package com.orangebox.kit.core.systemmessage

import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/systemMessage")
class SystemMessageRestService {

    @Inject
    private lateinit var systemMessageService: SystemMessageService

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save")
    fun save(systemMessage: SystemMessage){
        systemMessageService.save(systemMessage)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/checkAndSave")
    fun checkAndSave(systemMessage: SystemMessage){
        systemMessageService.checkAndSave(systemMessage.key!!,
            systemMessage.values!![0].language!!,
            systemMessage.values!![0].message!!)
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    fun list(): List<SystemMessage>? {
        return systemMessageService.list()
    }

    @GET
    @Path("/loadByKey/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    fun loadByKey(@PathParam("key") key: String): SystemMessage? {
        return systemMessageService.loadByKey(key)
    }
}