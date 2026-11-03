@spike.EntryPoint
interface App {
    val screen: Screen
    companion object
}

@spike.Include
class Screen(@Title val title: String)

@spike.Include
fun name(): String = "name"
