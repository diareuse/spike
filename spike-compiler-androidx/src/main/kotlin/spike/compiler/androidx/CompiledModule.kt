package spike.compiler.androidx

data class CompiledModule(private val name: String?) {
    val origin: String get() = name ?: "a dependency"
    val subject: String get() = name ?: "that dependency"
}
