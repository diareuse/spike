fun main() {
    check(App().screen.clock.name == "late") { "clock ${App().screen.clock.name}" }
}

annotation class GeneratesIncludeLater

@GeneratesIncludeLater
object Trigger

@spike.Include
class Screen(val clock: Clock)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
