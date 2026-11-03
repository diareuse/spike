@spike.EntryPoint
interface App {
    val screen: Screen
    companion object
}

@spike.Include
class Screen

@spike.Include
val theme: Theme
    get() = error("unused")
