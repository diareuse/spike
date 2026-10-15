package spike

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class Include(
    @Deprecated(
        message = "bindAs was removed. Bind with an @Include property or function, " +
            "e.g. @Include val Impl.asApi: Api get() = this",
        level = DeprecationLevel.ERROR
    )
    val bindAs: KClass<*> = Any::class,
    val bindTo: BindTarget = BindTarget.None
) {
    @Target(AnnotationTarget.CONSTRUCTOR)
    @Retention(AnnotationRetention.SOURCE)
    public annotation class Constructor
}