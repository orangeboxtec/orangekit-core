package com.orangebox.kit.core.bucket.aws.s3

import com.orangebox.kit.core.bucket.Bucket
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject


@ApplicationScoped
class S3Bucket : Bucket() {

    @Inject
    lateinit var s3Client: S3AsyncClient

    override fun saveFile(name: String, sufix: String?, data: ByteArray): String {
        val req = PutObjectRequest.builder()
            .bucket(params?.get("bucketName"))
            .key(name)
            .contentType(params?.get("contentType"))
            .contentLength(data.size.toLong())
            .build()

        s3Client.putObject(req, AsyncRequestBody.fromBytes(data))

        return "https://" + params?.get("bucketName") + ".s3-sa-east-1.amazonaws.com/" + name
    }

    override fun deleteFile(name: String) {
        val req = DeleteObjectRequest.builder()
            .bucket(params?.get("bucketName"))
            .key(name)
            .build()

        s3Client.deleteObject(req)
    }
}