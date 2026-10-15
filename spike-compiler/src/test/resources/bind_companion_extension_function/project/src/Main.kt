fun main() {
    require(Shelter().dog is Beagle)
}

@spike.EntryPoint
interface Shelter {
    val dog: Dog

    companion object
}

interface Dog {
    companion object {
        @spike.Include
        fun Beagle.asDog(): Dog = this
    }
}

@spike.Include
class Beagle : Dog
