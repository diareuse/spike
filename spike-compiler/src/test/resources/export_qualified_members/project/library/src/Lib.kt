@spike.Qualifier
annotation class Primary

@spike.Qualifier
annotation class Secondary

@spike.Export
interface Exported {
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
