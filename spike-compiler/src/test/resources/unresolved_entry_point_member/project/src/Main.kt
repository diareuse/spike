@spike.EntryPoint
interface App {
    val screen: Screen
    fun settings(): Settings
    val theme: Theme
    companion object
}

@spike.Include
class Screen
