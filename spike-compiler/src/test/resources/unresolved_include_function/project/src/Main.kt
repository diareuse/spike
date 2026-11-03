@spike.EntryPoint
interface App {
    val screen: Screen
    companion object
}

class Screen(val name: String)

@spike.Include
fun screen(settings: Map<String, Settings>): Screen = Screen("")

@spike.Include
fun settings(): Map<String, Settings> = emptyMap()
