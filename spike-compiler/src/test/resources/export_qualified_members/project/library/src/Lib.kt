@spike.Qualifier
annotation class Primary

@spike.Qualifier
annotation class Secondary

@spike.Qualifier
@Target(AnnotationTarget.TYPE)
annotation class Tertiary

@spike.Export
interface Exported {
    val tertiary: @Tertiary String

    @Primary
    val value: String

    @Secondary
    fun value(): String
}

@spike.Include
@Primary
fun primary(): String = "a"

@spike.Include
@Secondary
fun secondary(): String = "b"

@spike.Include
fun tertiary(): @Tertiary String = "c"
