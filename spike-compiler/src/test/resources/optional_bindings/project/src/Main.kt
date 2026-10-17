fun main() {
    val screen = App().screen
    check(screen.analytics == null) { "an unbound optional must be null" }
    check(screen.clock != null) { "a binding of Clock must satisfy Clock?" }
    check(screen.config?.name == "nullable binding") { "a binding of Config? must win" }
}

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}

class Analytics

@spike.Include
class Clock

class Config(val name: String)

@spike.Include
fun config(): Config? = Config("nullable binding")

@spike.Include
fun plainConfig(): Config = Config("plain binding")

@spike.Include
class Screen(val analytics: Analytics?, val clock: Clock?, val config: Config?)
