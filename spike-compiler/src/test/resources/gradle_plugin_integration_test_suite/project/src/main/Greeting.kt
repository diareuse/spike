@spike.Include
class Greeting {
    val text = "hello"
}

@spike.EntryPoint
interface Graph {
    val greeting: Greeting

    companion object
}
