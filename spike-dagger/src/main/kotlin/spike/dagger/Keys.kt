package spike.dagger

import spike.Key
import kotlin.reflect.KClass

@Key
@Retention(AnnotationRetention.BINARY)
public annotation class StringKey(val value: String)

@Key
@Retention(AnnotationRetention.BINARY)
public annotation class IntKey(val value: Int)

@Key
@Retention(AnnotationRetention.BINARY)
public annotation class LongKey(val value: Long)

@Key
@Retention(AnnotationRetention.BINARY)
public annotation class ClassKey(val value: KClass<*>)
