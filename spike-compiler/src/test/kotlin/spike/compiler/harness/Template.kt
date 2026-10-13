package spike.compiler.harness

enum class Template(val directory: String?) {
    MultiModule("multi_module"),
    Standalone(null)
}
