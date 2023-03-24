package com.orangebox.kit.core.bucket.aws.s3

import com.orangebox.kit.core.bucket.Bucket
import org.eclipse.microprofile.config.inject.ConfigProperty
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.lang.IllegalArgumentException
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject


@ApplicationScoped
class S3Bucket : Bucket() {

    @ConfigProperty(name = "orangekit.core.bucket.name", defaultValue = "ERROR")
    private lateinit var bucketName: String

    @ConfigProperty(name = "quarkus.s3.aws.region")
    private lateinit var region: String

    @Inject
    lateinit var s3Client: S3Client

    override fun saveFile(name: String, sufix: String?, data: ByteArray): String {

        if(bucketName == "ERROR"){
            throw IllegalArgumentException("orangekit.core.bucket.name must be provided in .env")
        }

        var fileName = name
        if(sufix != null){
            fileName = "$name/$sufix"
        }

        val req = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(params["contentType"])
            .contentLength(data.size.toLong())
            .acl("public-read")
            .build()

        val x = s3Client.putObject(req, RequestBody.fromBytes(data))
        return "https://$bucketName.s3-$region.amazonaws.com/$fileName"
    }

    override fun deleteFile(name: String) {
        val req = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(name)
            .build()

        s3Client.deleteObject(req)
    }
}