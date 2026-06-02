package spike

@Deprecated(
    message = "Use Include.Constructor instead",
    replaceWith = ReplaceWith("Include.Constructor", "spike.Include"),
    level = DeprecationLevel.ERROR
)
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.SOURCE)
public annotation class Inject
