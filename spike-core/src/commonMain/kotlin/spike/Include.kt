package spike

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
public annotation class Include(
    val bindAs: KClass<*> = Any::class,
    val bindTo: BindTarget = BindTarget.None
)