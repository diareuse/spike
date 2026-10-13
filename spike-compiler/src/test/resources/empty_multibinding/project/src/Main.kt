fun main() {
    val ep = PropertyEntryPoint()
    require(ep.set.isEmpty()) { "set was ${ep.set}" }
    require(ep.list.isEmpty()) { "list was ${ep.list}" }
    require(ep.map.isEmpty()) { "map was ${ep.map}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val set: Set<Plugin>
    val list: List<Plugin>
    val map: Map<String, Plugin>

    companion object
}

interface Plugin
