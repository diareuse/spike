package spike.compiler.harness

enum class Template(val directory: String?) {
    Jvm("jvm"),
    Androidx("androidx"),
    MultiModule("multi_module"),
    Standalone(null)
}
