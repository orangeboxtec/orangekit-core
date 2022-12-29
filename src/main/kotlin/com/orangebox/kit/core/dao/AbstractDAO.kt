package com.orangebox.kit.core.dao

import com.mongodb.BasicDBObject
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.orangebox.kit.core.annotation.OKDatabase
import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId
import com.orangebox.kit.core.dto.ResponseList
import org.apache.commons.lang3.reflect.FieldUtils
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.lang.reflect.Field
import java.util.*
import java.util.Map
import javax.inject.Inject

abstract class AbstractDAO<O>(klass: Class<O>) {

    @Inject
    private lateinit var mongoClient: MongoClient

    private var klass: Class<O>? = klass

    private var entityName: String? = null

    @ConfigProperty(name = "quarkus.mongodb.database")
    private lateinit var database: String

    init {
        if (klass.isAnnotationPresent(OKEntity::class.java)) {
            entityName = klass.getAnnotation(OKEntity::class.java).name
        } else {
            entityName = klass.simpleName
            entityName = entityName!!.lowercase() + entityName!!.substring(1)
        }

        if (klass.isAnnotationPresent(OKDatabase::class.java)) {
            database = klass.getAnnotation(OKDatabase::class.java).name
        }
    }


    open fun getId(bean: O): Any? {
        var fields: Array<Field> = FieldUtils.getFieldsWithAnnotation(
            klass,
            OKId::class.java
        )
        if (fields.isEmpty()) {
            fields = FieldUtils.getFieldsWithAnnotation(klass, OKId::class.java)
            if (fields.isEmpty()) {
                fields = arrayOf(FieldUtils.getDeclaredField(klass, "id"))
            }
        }
        return if (fields.isNotEmpty()) {
            FieldUtils.readDeclaredField(bean, fields[0].name, true)
        } else null
    }

    open fun setId(bean: O, id: String?) {
        var fields: Array<Field> = FieldUtils.getFieldsWithAnnotation(
            klass,
            OKId::class.java
        )
        if (fields.isEmpty()) {
            fields = FieldUtils.getFieldsWithAnnotation(klass, OKId::class.java)
            if (fields.isEmpty()) {
                fields = arrayOf(FieldUtils.getDeclaredField(klass, "id"))
            }
        }
        FieldUtils.writeDeclaredField(bean, fields[0].name, id, true)
    }

    private fun getDb(): MongoDatabase {
        return mongoClient.getDatabase(database)
    }

    open fun insert(bean: O) {
        if (getId(bean) == null) {
            setId(bean, UUID.randomUUID().toString())
        }
        val db: MongoDatabase = getDb()
        db.getCollection(entityName!!, klass!!).insertOne(bean)
    }

    open fun update(bean: O) {
        val db: MongoDatabase = getDb()
        db.getCollection(entityName!!, klass!!).replaceOne(Filters.eq("_id", getId(bean)), bean)
    }

    open fun retrieve(bean: O): O? {
        val db: MongoDatabase = getDb()
        val list = ArrayList<O>()
        db.getCollection(entityName!!, klass!!).find(BasicDBObject("_id", getId(bean))).into(list)
        return if (list.isEmpty()) {
            list[0]
        } else null
    }

    open fun retrieve(id: String): O? {
        val db: MongoDatabase = getDb()
        val list = ArrayList<O>()
        db.getCollection(entityName!!, klass!!).find(BasicDBObject("_id", id)).into(list)
        return if (list.isEmpty()) {
            list[0]
        } else null
    }

    open fun search(search: Search): List<O>? {
        val db = getDb()
        val list = ArrayList<O>()
        val find: FindIterable<O> = db.getCollection(entityName!!, klass!!).find(search.document!!)
        if (search.sortDoc != null) {
            find.sort(search.sortDoc)
        }
        if (search.first != null) {
            find.skip(search.first!!)
        }
        if (search.maxResults != null) {
            find.limit(search.maxResults!!)
        }
        find.into(list)
        return list
    }

    open fun retrieve(search: Search): O? {
        val list = search(search)
        return if (list?.isNotEmpty() == true) {
            list[0]
        } else null
    }

    open fun retrieveFirst(): O? {
        val db = getDb()
        val list = ArrayList<O>()
        db.getCollection(entityName!!, klass!!).find().into(list)
        return if (list.isNotEmpty()) {
            list[0]
        } else null
    }

    open fun delete(bean: O) {
        val db: MongoDatabase = getDb()
        db.getCollection(entityName!!, klass!!).deleteOne(Filters.eq("_id", getId(bean)))
    }

    open fun createBuilder(): SearchBuilder {
        return SearchBuilder()
    }

    open fun listAll(): List<O>? {
        val db: MongoDatabase = getDb()
        val list = ArrayList<O>()
        db.getCollection(entityName!!, klass!!).find().into(list)
        return list
    }

    open fun retrieveByNativeField(field: String?, value: String?): O? {
        val db: MongoDatabase = getDb()
        val list = ArrayList<O>()
        (Map.ofEntries(
            Map.entry(field, value)
        ) as kotlin.collections.Map<*, *>?)?.let {
            BasicDBObject(
                it
            )
        }?.let {
            db.getCollection(entityName!!, klass!!).find(
                it
            ).into(list)
        }
        return if (list.isNotEmpty()) {
            list[0]
        } else null
    }

    open fun count(search: Search): Long {
        val db: MongoDatabase = getDb()
        return db.getCollection(entityName!!, klass!!).countDocuments(search.document!!)
    }

    private fun pageQuantity(numberOfItensByPage: Int, totalAmount: Long): Long {
        val pageQuantity: Long = if (totalAmount % numberOfItensByPage != 0L) {
            totalAmount / numberOfItensByPage + 1
        } else {
            totalAmount / numberOfItensByPage
        }
        return pageQuantity
    }

    open fun searchToResponse(search: Search): ResponseList<O>? {
        val list = search(search)
        val totalAmount: Long = count(search)
        val pageQuantity: Long
        if (search.maxResults != null && search.maxResults!! > 0) {
            pageQuantity = this.pageQuantity(search.maxResults!!, totalAmount)
        } else {
            pageQuantity = this.pageQuantity(10, totalAmount)
        }
        val result: ResponseList<O> = ResponseList()
        result.list = list
        result.totalAmount = totalAmount
        result.pageQuantity = pageQuantity
        return result
    }
}