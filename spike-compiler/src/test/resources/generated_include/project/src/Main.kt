fun main() {
    check(App().screen.clock.name == "generated") { "clock ${App().screen.clock.name}" }
}

annotation class GeneratesInclude

@GeneratesInclude
class Clock {
    val name = "generated"
}

@spike.Include
class Screen(val clock: Clock)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
