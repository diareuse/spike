fun main() {
    require(PropertyEntryPoint().consumer.lib.name == "lib")
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val consumer: Consumer

    companion object
}

@spike.Include
class Consumer(val lib: Lib)
