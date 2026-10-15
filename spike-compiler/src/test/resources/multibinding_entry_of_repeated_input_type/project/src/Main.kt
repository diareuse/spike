fun main() {
    val tokens = Tokens.factory().create("a", "b").tokens
    require(tokens == setOf("extra")) { "set held $tokens" }
}

@spike.EntryPoint
interface Tokens {
    val tokens: Set<String>

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(primary: String, secondary: String): Tokens
    }

    companion object
}

@spike.Include(bindTo = spike.BindTarget.Set)
fun extra(): String = "extra"
