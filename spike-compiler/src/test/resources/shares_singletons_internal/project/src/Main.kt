@spike.EntryPoint
interface PropertyEntryPoint {
    val client: Client

    @spike.EntryPoint.Factory.SharesSingletons
    @spike.EntryPoint.Factory
    interface Factory {
        fun create(config: String): PropertyEntryPoint
    }

    companion object
}

@spike.Include
class Client(val config: String)
