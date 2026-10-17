package spike.metro

import spike.Key
import spike.Qualifier
import kotlin.reflect.KClass

@Qualifier
@Retention(AnnotationRetention.BINARY)
public annotation class Named(val value: String)

@Key
@Retention(AnnotationRetention.BINARY)
public annotation class StringKey(val value: String)

@Key
@Retention(AnnotationRetention.BINARY)
public annotation class IntKey(val value: Int)

@Key
@Retention(AnnotationRetention.BINARY)
public annotation class ClassKey(val value: KClass<*>)
