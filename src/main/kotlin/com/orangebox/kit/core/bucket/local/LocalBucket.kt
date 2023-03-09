package com.orangebox.kit.core.bucket.local

import com.orangebox.kit.core.bucket.Bucket
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class LocalBucket : Bucket() {

    override fun saveFile(name: String, sufix: String?, data: ByteArray): String {
        val path: Path = Paths.get(params?.get("folder") + "/" + name + sufix)
        Files.createDirectories(path.parent)
        Files.write(path, data)
        return params?.get("urlBase") + "/" + name
    }

    override fun deleteFile(name: String) {
        FileUtils.forceDelete(File(params?.get("folder") + "/" + name))
    }
}