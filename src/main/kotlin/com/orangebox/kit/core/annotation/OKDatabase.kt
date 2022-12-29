package com.orangebox.kit.core.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class OKDatabase(val name: String)
