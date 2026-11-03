@spike.EntryPoint
interface App {
    val screen: Screen
    fun settings(): Settings
    companion object
}

@spike.Include
class Screen
