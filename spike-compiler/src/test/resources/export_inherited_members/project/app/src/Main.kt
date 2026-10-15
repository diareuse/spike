fun main() {
    Consuming().consumer
}

@spike.EntryPoint
interface Consuming {
    val consumer: Consumer

    companion object
}

@spike.Include
class Consumer(val lib: Lib)
