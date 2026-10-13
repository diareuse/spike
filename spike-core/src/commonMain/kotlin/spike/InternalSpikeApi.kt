package spike

@RequiresOptIn(
    message = "This is used by code Spike generates and may change without notice. Do not use it directly.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
public annotation class InternalSpikeApi
