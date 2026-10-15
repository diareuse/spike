@spike.EntryPoint
interface PropertyEntryPoint {
    val plugins: Map<Int, Plugin>

    companion object
}

@spike.Key
annotation class StringKey(val name: String)

interface Plugin

class LoggingPlugin : Plugin

@StringKey("logging")
@spike.Include(bindTo = spike.BindTarget.Map)
fun loggingPlugin(): Plugin = LoggingPlugin()
