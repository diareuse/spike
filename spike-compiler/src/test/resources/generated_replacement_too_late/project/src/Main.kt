class Clock

@spike.Include
class Screen

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
