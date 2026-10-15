@spike.EntryPoint
interface Host {
    @Kind("core")
    val plugins: Map<String, Plugin>

    companion object
}

@spike.Qualifier
annotation class Kind(val name: String)

@spike.Key
annotation class StringKey(val name: String)

interface Plugin

@StringKey("logging")
@Kind("core")
@spike.Include(bindTo = spike.BindTarget.Map, bindAs = Plugin::class)
class LoggingPlugin : Plugin
