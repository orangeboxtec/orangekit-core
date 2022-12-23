package com.orangebox.kit.core.dao

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId
import org.apache.commons.lang3.reflect.FieldUtils
import java.lang.reflect.Field
import java.util.*
import javax.inject.Inject

abstract class AbstractDAO<O>(klass: Class<O>) {

    @Inject
    private lateinit var mongoClient: MongoClient

    private var klass: Class<O>? = klass

    private var entityName: String? = null

    init {
        if (klass.isAnnotationPresent(OKEntity::class.java)) {
            entityName = klass.getAnnotation(OKEntity::class.java).name
        } else {
            entityName = klass.simpleName
            entityName = entityName!!.lowercase() + entityName!!.substring(1)
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
            org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField(bean, fields[0].name, true)
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
        return mongoClient.getDatabase("orangekit")
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

    @Throws(Exception::class)
    open fun retrieve(bean: O): O? {
        val db: MongoDatabase = getDb()
        val list = ArrayList<O>()
        db.getCollection(entityName!!, klass!!).find(BasicDBObject("_id", getId(bean))).into(list)
        return if (!CollectionUtils.isEmpty(list)) {
            list[0]
        } else null
    }
}