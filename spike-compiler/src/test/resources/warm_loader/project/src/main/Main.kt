@spike.Include
class Greeting {
    val text = "first"
}

@spike.EntryPoint
interface Application {
    val greeting: Greeting

    companion object
}

fun main() {
    println(Application().greeting.text)
}
