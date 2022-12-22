package com.orangebox.kit.core

import com.orangebox.kit.core.configuration.Configuration
import com.orangebox.kit.core.configuration.ConfigurationService
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/hello")
class ExampleResource {

    @Inject
    private lateinit var configurationService: ConfigurationService

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "Hello from RESTEasy Reactive"

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addConfiguration")
    @Throws(Exception::class)
    fun addConfiguration(configuration: Configuration){
        configurationService.add(configuration)
    }
}