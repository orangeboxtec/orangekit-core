package com.orangebox.kit.core.utils

import java.io.InputStream

interface PropertiesGeneralProcesser {
    fun loadPropertieStream(path: String?): InputStream?
}