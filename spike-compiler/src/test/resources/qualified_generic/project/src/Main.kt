fun main() {
    check(Names().names == listOf("a", "b")) { "names ${Names().names}" }
}

@spike.Qualifier
annotation class Greeting

@spike.EntryPoint
interface Names {
    @Greeting
    val names: List<String>

    companion object
}

@Greeting
@spike.Include
fun names(): List<String> = listOf("a", "b")
