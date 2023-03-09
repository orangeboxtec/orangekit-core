package com.orangebox.kit.core.photo

import org.bson.codecs.pojo.annotations.BsonProperty

class GalleryItem {

    @BsonProperty("_id")
    var id: String? = null

    var name: String? = null

    var urlCover: String? = null

    var urlFile: String? = null

    var index: Int? = null

    constructor()
    constructor(id: String?) {
        this.id = id
    }
}