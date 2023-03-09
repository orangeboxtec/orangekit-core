package com.orangebox.kit.core.utils

import com.orangebox.kit.core.dao.AbstractDAO

class BusinessUtils<O>(dao: AbstractDAO<O>) {
    private val dao: AbstractDAO<O>

    init {
        this.dao = dao
    }

    @Throws(Exception::class)
    fun basicSave(bean: O) {
        if (dao.getId(bean) == null) {
            dao.insert(bean)
        } else {
            val beanBase: O? = dao.retrieve(bean)
            if (beanBase != null) {
                for (field in bean.javaClass.getDeclaredFields()) {
                    field.isAccessible = true
                    val obj = field[bean]
                    if (obj != null) {
                        field[beanBase] = obj
                    }
                }
                dao.update(beanBase)
            }
        }
    }

    @Throws(Exception::class)
    fun basicCopyObject(bean: O): O? {
        var beanBase: O? = null
        if (dao.getId(bean) == null) {
            return bean
        } else {
            beanBase = dao.retrieve(bean)
            if (beanBase != null) {
                for (field in bean.javaClass.getDeclaredFields()) {
                    field.isAccessible = true
                    val obj = field[bean]
                    if (obj != null) {
                        field[beanBase] = obj
                    }
                }
            }
        }
        return beanBase
    }
}