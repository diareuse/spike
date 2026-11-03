@spike.EntryPoint
interface App {
    val screen: Screen
    companion object
}

@spike.Include
class Screen : Destination
