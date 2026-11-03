package com.example.platform

import kotlin.reflect.KClass

@Retention(AnnotationRetention.BINARY)
annotation class Keep(val reason: KClass<*> = Any::class)

class Reason
