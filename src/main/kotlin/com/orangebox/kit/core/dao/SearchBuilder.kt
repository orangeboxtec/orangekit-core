package com.orangebox.kit.core.dao

import org.bson.Document
import java.util.*

class SearchBuilder {

    private val search: Search = Search()

    init {
        search.document = Document()
    }

    fun appendParamQuery(key: String?, value: Any): SearchBuilder? {
        var value = value
        if (value.javaClass.isEnum) {
            value = value.toString()
        }
        search.document!!.append(key, value)
        return this
    }

    fun appendParamQuery(key: String, value: Any, operation: OperationEnum?): SearchBuilder? {
        var value = value
        if (value.javaClass.isEnum) {
            value = value.toString()
        }
        when (operation) {
            OperationEnum.LIKE -> search.document!!.append(key, Document("\$regex", "$value.*").append("\$options", "i"))
            OperationEnum.GT -> search.document!!.append(key, Document("\$gt", value))
            OperationEnum.GTE -> search.document!!.append(key, Document("\$gte", value))
            OperationEnum.LT -> search.document!!.append(key, Document("\$lt", value))
            OperationEnum.LTE -> search.document!!.append(key, Document("\$lte", value))
            OperationEnum.NOT -> search.document!!.append(key, Document("\$ne", value))
            OperationEnum.OR_FIELDS -> {
                val keys = key.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val orDoc = ArrayList<Document>()
                var i = 0
                while (i < keys.size) {
                    orDoc.add(Document(keys[i], value))
                    i++
                }
                search.document!!.append("\$or", orDoc)
            }

            OperationEnum.OR_FIELDS_LIKE -> {
                val keys = key.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val orDoc = ArrayList<Document>()
                var i = 0
                while (i < keys.size) {
                    orDoc.add(Document(keys.get(i), Document("\$regex", "$value.*").append("\$options", "i")))
                    i++
                }
                search.document!!.append("\$or", orDoc)
            }

            OperationEnum.IN -> search.document!!.append(key, Document("\$in", value))
            OperationEnum.NIN -> search.document!!.append(key, Document("\$nin", value))
            OperationEnum.GEO -> {
                val let = value as ArrayList<*>
                search.document!!.append(
                    key, Document(
                        "\$near", Document(
                            "\$geometry", Document("type", "Point")
                                .append("coordinates", Arrays.asList(let[0], let[1]))
                        )
                            .append("\$maxDistance", let[2])
                    )
                )
            }

            OperationEnum.EXISTS -> search.document!!.append(key, Document("\$exists", value))
            OperationEnum.TYPE -> search.document!!.append(key, Document("\$type", value))
            else -> {}
        }
        return this
    }

    fun appendParamQuery(key: String?, value1: Any, value2: Any, operation: OperationEnum?): SearchBuilder? {
        var value1 = value1
        var value2 = value2
        if (value1.javaClass.isEnum) {
            value1 = value1.toString()
        }
        if (value2.javaClass.isEnum) {
            value2 = value2.toString()
        }
        when (operation) {
            OperationEnum.BETWEEN -> search.document!!.append(key, Document("\$gt", value1).append("\$lt", value2))
            OperationEnum.RANGE -> search.document!!.append(key, Document("\$gte", value1).append("\$lte", value2))
            else -> {}
        }
        return this
    }

    fun appendMap(map: Map<String?, Any?>?): SearchBuilder? {
        if (map != null) {
            for (key in map.keys) {
                appendParamQuery(key, map[key]!!)
            }
        }
        return this
    }

    fun appendSort(key: String?, value: Int?): SearchBuilder? {
        if (search.sortDoc == null) {
            search.sortDoc = Document()
        }
        search.sortDoc!!.append(key, value)
        return this
    }

    fun setFirst(first: Int?): SearchBuilder? {
        search.first = first
        return this
    }

    fun setMaxResults(maxResults: Int?): SearchBuilder? {
        search.maxResults = maxResults
        return this
    }

    fun setProjection(projection: SearchProjection?): SearchBuilder? {
        search.projection = projection
        return this
    }

    fun setQuery(query: Document?): SearchBuilder? {
        search.document = query
        return this
    }

    fun build(): Search {
        return search
    }
}