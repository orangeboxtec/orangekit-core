package com.orangebox.kit.core.bucket.local

import com.orangebox.kit.core.bucket.Bucket
import org.apache.commons.io.FileUtils
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LocalBucket : Bucket() {


    @ConfigProperty(name = "orangekit.core.bucket.local.folder", defaultValue = "ERROR")
    private lateinit var folder: String

    @ConfigProperty(name = "orangekit.core.bucket.local.urlbase", defaultValue = "ERROR")
    private lateinit var urlBase: String


    override fun saveFile(name: String, sufix: String?, data: ByteArray): String {
        checkVariables()

        val pathStr = if(params["folder"] != null){
            val subFolder = params["folder"]
            "$folder/$subFolder/$name$sufix"
        } else{
            "$folder/$name$sufix"
        }

        val path: Path = Paths.get(pathStr)
        Files.createDirectories(path.parent)
        Files.write(path, data)
        return "$urlBase/$name"
    }

    override fun deleteFile(name: String) {
        checkVariables()
        FileUtils.forceDelete(File("$folder/$name"))
    }

    private fun checkVariables(){
        if(folder == "ERROR"){
            throw IllegalArgumentException("orangekit.core.bucket.local.folder must be provided in .env")
        }
        if(urlBase == "ERROR"){
            throw IllegalArgumentException("orangekit.core.bucket.local.urlbase must be provided in .env")
        }
    }
}