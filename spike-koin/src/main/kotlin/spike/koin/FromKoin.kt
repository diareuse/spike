package spike.koin

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
public annotation class FromKoin(vararg val types: KClass<*>)
