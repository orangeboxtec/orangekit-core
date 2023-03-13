package com.orangebox.kit.core.bucket

abstract class Bucket {

    var params: HashMap<String, String> = HashMap()
    abstract fun saveFile(name: String, sufix: String?, data: ByteArray): String?
    abstract fun deleteFile(name: String)
    fun withParams(params: HashMap<String, String>): Bucket {
        this.params = params
        return this
    }
}