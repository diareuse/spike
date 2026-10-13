@spike.EntryPoint
interface PropertyEntryPoint {
    val plugins: Map<Int, Plugin>

    companion object
}

@spike.Key
annotation class StringKey(val name: String)

interface Plugin

@StringKey("logging")
@spike.Include(bindTo = spike.BindTarget.Map, bindAs = Plugin::class)
class LoggingPlugin : Plugin
