package com.orangebox.kit.core.configuration

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoCursor
import org.bson.Document
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ConfigurationService {

    @Inject
    private lateinit var mongoClient: MongoClient

    fun list(): List<Configuration?>? {
        val list = ArrayList<Configuration>()
        val cursor: MongoCursor<Document> = getCollection().find().iterator()
        try {
            while (cursor.hasNext()) {
                val document: Document = cursor.next()
                val configuration = Configuration()
                configuration.key = document.getString("name")
                configuration.value = document.getString("description")
                list.add(configuration)
            }
        } finally {
            cursor.close()
        }
        return list
    }

    fun add(configuration: Configuration) {
        val document = Document()
            .append("key", configuration.key)
            .append("value", configuration.value)
        getCollection().insertOne(document)
    }

    private fun getCollection(): MongoCollection<Document> {
        return mongoClient.getDatabase("orangekit").getCollection("configuration")
    }
}