@spike.EntryPoint
interface Greeter {
    val greeting: Greeting

    companion object
}

@spike.Include
class Greeting {
    val text = "hello"
}
