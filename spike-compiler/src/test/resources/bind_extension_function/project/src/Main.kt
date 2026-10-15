fun main() {
    require(Shelter().dog is Beagle)
}

@spike.EntryPoint
interface Shelter {
    val dog: Dog

    companion object
}

interface Dog

@spike.Include
class Beagle : Dog

@spike.Include
fun Beagle.asDog(): Dog = this
