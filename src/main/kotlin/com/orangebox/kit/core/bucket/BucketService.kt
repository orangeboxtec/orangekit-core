package com.orangebox.kit.core.bucket

import com.orangebox.kit.core.bucket.aws.s3.S3Bucket
import com.orangebox.kit.core.bucket.local.LocalBucket
import com.orangebox.kit.core.configuration.Configuration
import com.orangebox.kit.core.configuration.ConfigurationService
import com.orangebox.kit.core.photo.PhotoUpload
import net.coobird.thumbnailator.Thumbnails
import org.apache.commons.codec.binary.Base64
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.enterprise.context.ApplicationScoped
import javax.imageio.ImageIO
import javax.inject.Inject

@ApplicationScoped
class BucketService {

    @Inject
    private lateinit var configurationService: ConfigurationService

    @Inject
    private lateinit var s3Bucket: S3Bucket

    fun saveImage(photoUpload: PhotoUpload, pathFolder: String): String? {
        return saveImage(photoUpload, pathFolder, null)
    }

    @Throws(Exception::class)
    fun saveImage(photoUpload: PhotoUpload, pathFolder: String, namePrefix: String?): String? {
        val bucket = bucket
        bucket.params?.set("folder", pathFolder)
        bucket.params?.set("contentType", "image/jpg")
        var sufix: String
        sufix = if (namePrefix != null) {
            "_original.jpg"
        } else {
            "original.jpg"
        }

        //write the original image
        var data: ByteArray? = null
        data = photoUpload.photoBytes

        //create the crop version
        sufix = if (namePrefix != null) {
            "_main.jpg"
        } else {
            "main.jpg"
        }
        var bais: ByteArrayInputStream? = ByteArrayInputStream(data)
        val bimg: BufferedImage = ImageIO.read(bais)
        val width: Double = bimg.getWidth().toDouble()
        bais = ByteArrayInputStream(data)
        var scale = 1.0
        if (photoUpload.finalWidth != null) {
            scale = photoUpload.finalWidth!! * 100 / width / 100
        }
        val b: Thumbnails.Builder<out InputStream?> = Thumbnails.of(bais)
            .scale(scale)
        if (photoUpload.x != null) {
            b.sourceRegion(
                photoUpload.x!!.toInt(), photoUpload.y!!.toInt(),
                photoUpload.width!!.toInt(), photoUpload.height!!.toInt()
            )
        }
        b.outputFormat("jpg")
        if (photoUpload.rotate != null) {
            b.rotate(photoUpload.rotate!!.toDouble())
        }
        val baos = ByteArrayOutputStream()
        b.toOutputStream(baos)
        return bucket.saveFile(namePrefix!!, sufix, baos.toByteArray())
    }

    @Throws(Exception::class)
    fun saveVideo(photoUpload: PhotoUpload, pathFolder: String, namePrefix: String): String? {
        val bucket = bucket
        bucket.params?.set("folder", pathFolder)
        bucket.params?.set("contentType", "video/mp4")
        val sufix = "_original.mp4"

        //write the original image
        val data: ByteArray
        data = if (photoUpload.photoBytes != null) {
            photoUpload.photoBytes!!
        } else {
            Base64.decodeBase64(photoUpload.photo)
        }

        //escreve o original do disco
        return bucket.saveFile(namePrefix, sufix, data)
    }

    @Throws(Exception::class)
    fun saveFile(
        photoUpload: PhotoUpload,
        pathFolder: String,
        namePrefix: String,
        contentType: String
    ): String? {
        var namePrefix: String? = namePrefix
        val bucket = bucket
        bucket.params?.set("folder", pathFolder)
        bucket.params?.set("contentType", contentType)
        if (namePrefix == null) {
            namePrefix = "file"
        }

        //write the original image
        val data: ByteArray
        data = if (photoUpload.photoBytes != null) {
            photoUpload.photoBytes!!
        } else {
            Base64.decodeBase64(photoUpload.photo)
        }
        return bucket.saveFile(namePrefix, "", data)
    }


    fun deleteImage(folder: String, name: String) {
        val bucket = bucket
        bucket.params?.set("folder", folder)
        bucket.deleteFile(name)
    }

    protected val bucket: Bucket
        get() {
            val conf: Configuration? = configurationService.loadByCode("BUCKET_CONFIG")
            val type: String? = conf?.valueData?.get("type")
            return if (type == "S3") {
                s3Bucket.withParams(conf.valueData!!)
            } else {
                LocalBucket().withParams(conf?.valueData!!)
            }
        }
}