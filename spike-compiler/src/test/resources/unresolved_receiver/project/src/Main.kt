@spike.EntryPoint
interface App {
    val screen: Screen
    companion object
}

class Screen

@spike.Include
fun Navigator.screen(): Screen = Screen()
