package com.orangebox.kit.core.bucket

import com.orangebox.kit.core.bucket.aws.s3.S3Bucket
import com.orangebox.kit.core.bucket.local.LocalBucket
import com.orangebox.kit.core.photo.FileUpload
import net.coobird.thumbnailator.Thumbnails
import org.apache.commons.codec.binary.Base64
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.enterprise.context.ApplicationScoped
import javax.imageio.ImageIO
import javax.inject.Inject

@ApplicationScoped
class BucketService {

    @ConfigProperty(name = "orangekit.core.bucket.type", defaultValue = "LOCAL")
    private lateinit var bucketType: String

    @Inject
    private lateinit var s3Bucket: S3Bucket

    @Inject
    private lateinit var localBucket: LocalBucket



    fun saveImage(fileUpload: FileUpload, pathFolder: String): String? {
        return saveImage(fileUpload, pathFolder, null)
    }

    fun saveImage(fileUpload: FileUpload, pathFolder: String, namePrefix: String?): String? {
        val bucket = bucket
        bucket.params["folder"] = pathFolder
        bucket.params["contentType"] = "image/jpg"

        var name = namePrefix
        if(name == null){
            name = fileUpload.title
        }

        //write the original image
        val data: ByteArray? = fileUpload.fileBytes

        //create the crop version
        val sufix: String = if (namePrefix != null) {
            "_main.jpg"
        } else {
            "main.jpg"
        }
        var bais: ByteArrayInputStream? = ByteArrayInputStream(data)
        val bimg: BufferedImage = ImageIO.read(bais)
        val width: Double = bimg.width.toDouble()
        bais = ByteArrayInputStream(data)
        var scale = 1.0
        if (fileUpload.finalWidth != null) {
            scale = fileUpload.finalWidth!! * 100 / width / 100
        }
        val b: Thumbnails.Builder<out InputStream?> = Thumbnails.of(bais)
            .scale(scale)
        if (fileUpload.x != null) {
            b.sourceRegion(
                fileUpload.x!!.toInt(), fileUpload.y!!.toInt(),
                fileUpload.width!!.toInt(), fileUpload.height!!.toInt()
            )
        }
        b.outputFormat("jpg")
        if (fileUpload.rotate != null) {
            b.rotate(fileUpload.rotate!!.toDouble())
        }
        val baos = ByteArrayOutputStream()
        b.toOutputStream(baos)
        return bucket.saveFile(name!!, sufix, baos.toByteArray())
    }

    fun saveVideo(fileUpload: FileUpload, pathFolder: String, namePrefix: String): String? {
        val bucket = bucket
        bucket.params["folder"] = pathFolder
        bucket.params["contentType"] = "video/mp4"
        val sufix = "_original.mp4"

        //write the original image
        val data: ByteArray
        data = if (fileUpload.fileBytes != null) {
            fileUpload.fileBytes!!
        } else {
            Base64.decodeBase64(fileUpload.file)
        }

        //escreve o original do disco
        return bucket.saveFile(namePrefix, sufix, data)
    }

    fun saveFile(
        fileUpload: FileUpload,
        pathFolder: String,
        namePrefix: String?,
        contentType: String
    ): String? {
        var namePrefix = namePrefix
        val bucket = bucket
        bucket.params["folder"] = pathFolder
        bucket.params["contentType"] = contentType
        if (namePrefix == null) {
            namePrefix = "file"
        }

        //write the original image
        val data: ByteArray = if (fileUpload.fileBytes != null) {
            fileUpload.fileBytes!!
        } else {
            Base64.decodeBase64(fileUpload.file)
        }
        return bucket.saveFile(namePrefix, "", data)
    }


    fun deleteImage(folder: String, name: String) {
        val bucket = bucket
        bucket.params["folder"] = folder
        bucket.deleteFile(name)
    }

    protected val bucket: Bucket
        get() {
            return if (bucketType == "S3") {
                s3Bucket
            } else {
                localBucket
            }
        }
}