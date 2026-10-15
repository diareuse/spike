@spike.EntryPoint
interface Configured {
    val client: Client

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(config: String): Configured
    }

    companion object
}

@spike.Include
class Client(val config: String)

@spike.Include
fun defaultConfig(): String = "default"
