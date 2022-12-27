package com.orangebox.kit.core.dao

import org.bson.Document

class Search {

    var document: Document? = null

    var sortDoc: Document? = null

    var first: Int? = null

    var maxResults: Int? = null

    var projection: SearchProjection? = null
}