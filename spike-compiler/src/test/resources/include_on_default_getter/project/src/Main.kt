class Clock

@spike.Include
class Screen

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}

@get:spike.Include
val clock: Clock = Clock()
