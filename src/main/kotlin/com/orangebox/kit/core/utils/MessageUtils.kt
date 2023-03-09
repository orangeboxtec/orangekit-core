package com.orangebox.kit.core.utils

import java.text.DateFormat
import java.text.MessageFormat
import java.util.*

object MessageUtils {
    @Throws(Exception::class)
    fun message(language: String, key: String?, vararg args: Any?): String {
        val message: String
        val infoLanguage = language.split("(_|\\-)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val locale = Locale(infoLanguage[0], infoLanguage[1])
        val bundle = ResourceBundle.getBundle("ApplicationEJB", locale)
        message = MessageFormat.format(bundle.getString(key) as String, *args)
        return message
    }

    @Throws(Exception::class)
    fun dateFormat(language: String, style: Int): DateFormat? {
        var df: DateFormat? = null
        val infoLanguage = language.split("(_|\\-)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val locale = Locale(infoLanguage[0], infoLanguage[1])
        df = DateFormat.getDateInstance(style, locale)
        return df
    }
}