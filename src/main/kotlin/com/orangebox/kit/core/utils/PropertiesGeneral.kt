package com.orangebox.kit.core.utils

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

class PropertiesGeneral {
    /**
     * Carrega o arquivo de Properties, dado o path
     *
     * @param name
     * @return
     */
    @Throws(Exception::class)
    fun carregarProperties(path: String?, nome: String) {
        val arquivo = File(path, nome)
        if (!arquivo.exists()) {
            throw Exception("Arquivo propertie nao encontrado: $nome")
        }
        val `is`: InputStream = FileInputStream(arquivo)
        val properties = Properties()
        properties.load(`is`)
        props[nome] = properties
    }

    /**
     * Carrega o arquivo de Properties, dado o path
     *
     * @param name
     * @return
     */
    @Throws(Exception::class)
    private fun loadProperties(path: String): Properties {
        val properties = Properties()
        var `in`: InputStream? = null
        if (processers.size > 0) {
            for (processer in processers.values) {
                `in` = processer.loadPropertieStream(path)
                if (`in` != null) {
                    break
                }
            }
        } else {
            `in` = this.javaClass.getResourceAsStream(path)
        }
        properties.load(`in`)
        return properties
    }

    companion object {
        private val props: MutableMap<String, Properties?> = HashMap()
        var processers: Map<String, PropertiesGeneralProcesser> = HashMap()

        /**
         * Retorna a query de acordo com a chave passada como parametro
         * @param key
         * @return String
         * @throws Exception
         */
        fun getKey(propKey: String, key: String?): String? {
            var value: String? = null
            try {
                value = getProperties(propKey)!!.getProperty(key)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }

        fun getProperties(propertiesName: String): Properties? {
            try {
                if (props[propertiesName] == null) {
                    val prop = PropertiesGeneral().loadProperties("/$propertiesName.properties")
                        ?: throw Exception("Arquivo propertie nao encontrado: $propertiesName")
                    props[propertiesName] = prop
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return props[propertiesName]
        }

        @Throws(Exception::class)
        fun carregarPropertieParaSystem(caminhoPropertie: String?) {
            val arquivo = File(caminhoPropertie)
            if (!arquivo.exists()) {
                throw Exception("Arquivo properties nao foi encontrado: " + arquivo.canonicalPath)
            }


            // Carrega propriedades da configuracao
            val `is`: InputStream = FileInputStream(arquivo)
            val properties = Properties()
            properties.load(`is`)


            // Carrega variaveis de ambiente
            val keys = properties.keys()
            while (keys.hasMoreElements()) {
                val key = keys.nextElement().toString()
                System.setProperty(key, properties.getProperty(key))
            }
        }
    }
}