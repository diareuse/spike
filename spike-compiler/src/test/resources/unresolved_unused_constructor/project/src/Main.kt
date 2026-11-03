@spike.EntryPoint
interface App {
    val screen: Screen
    companion object
}

@spike.Include
class Screen @spike.Include.Constructor constructor(val name: String) {
    private constructor(context: Context) : this("")
}

@spike.Include
fun name(): String = "name"
