package com.orangebox.kit.core.configuration

import com.orangebox.kit.core.bucket.BucketService
import com.orangebox.kit.core.file.FileUpload
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/configuration")
class ConfigurationRestService {

    @Inject
    private lateinit var configurationService: ConfigurationService

    @Inject
    private lateinit var bucketService: BucketService

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


    @POST
    @Consumes("application/json")
    @Path("/saveFile")
    fun saveAvatar(fileUpload: FileUpload): String? {
        return bucketService.saveFile(fileUpload, "userb", null, "image/jpg")
    }
}