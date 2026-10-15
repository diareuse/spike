@spike.EntryPoint
interface Configured {
    val client: Client

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(primary: String, secondary: String): Configured
    }

    companion object
}

@spike.Include
class Client(val config: String)
