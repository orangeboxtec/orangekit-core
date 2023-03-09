package com.orangebox.kit.core.utils

import javax.ejb.Local

@Local
interface TypeService {
    fun listTypes(): List<String?>?
}