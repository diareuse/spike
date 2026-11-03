fun main() {
    check(IntegrationGraph().greeting.text == "hello")
}

@spike.EntryPoint
interface IntegrationGraph : Graph {
    companion object
}
