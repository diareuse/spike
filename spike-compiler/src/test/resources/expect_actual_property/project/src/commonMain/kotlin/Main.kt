@spike.EntryPoint
interface Platform {
    val greeting: Greeting

    companion object
}

class Greeting(val text: String)

@spike.Include
expect val greeting: Greeting
