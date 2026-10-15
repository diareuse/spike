fun main() {
    val graph = Tokens.factory().create("given")
    require(graph.token == "given") { "plain token was ${graph.token}" }
    require(graph.tokens == setOf("extra")) { "set held ${graph.tokens}" }
}

@spike.EntryPoint
interface Tokens {
    val token: String
    val tokens: Set<String>

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(token: String): Tokens
    }

    companion object
}

@spike.Include(bindTo = spike.BindTarget.Set)
fun extra(): String = "extra"
