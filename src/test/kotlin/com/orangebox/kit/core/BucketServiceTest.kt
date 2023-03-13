package com.orangebox.kit.core

import com.orangebox.kit.core.bucket.BucketService
import com.orangebox.kit.core.photo.FileUpload
import io.quarkus.test.junit.QuarkusTest
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import javax.inject.Inject

@QuarkusTest
class BucketServiceTest {

    @ConfigProperty(name = "test.core.fileupload.path")
    lateinit var fileUploadPath: String

    @Inject
    lateinit var bucketService: BucketService

    fun loadFile(): FileUpload{
        return FileUpload().apply {
            this.title = "Test"
            this.fileBytes = File(fileUploadPath).readBytes()
        }
    }

    @Test
    fun testUpload() {
        Assertions.assertDoesNotThrow { bucketService.saveImage(loadFile(), "users") }
    }

    @Test
    fun testDelete() {
        bucketService.deleteImage("users", "Testmain.jpg")
    }
}