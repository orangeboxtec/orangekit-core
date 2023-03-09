package com.orangebox.kit.core.configuration

import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/configuration")
class ConfigurationRestService {

    @Inject
    private lateinit var configurationService: ConfigurationService

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "Hello from Configuration Service!"

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save")
    @Throws(Exception::class)
    fun save(configuration: Configuration){
        configurationService.save(configuration)
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    fun list(): List<Configuration>? {
        return configurationService.list()
    }
}