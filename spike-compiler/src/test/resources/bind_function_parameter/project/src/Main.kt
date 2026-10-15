fun main() {
    require(Shelter().bird is Parrot)
}

@spike.EntryPoint
interface Shelter {
    val bird: Bird

    companion object
}

interface Bird

@spike.Include
class Parrot : Bird

@spike.Include
fun bird(parrot: Parrot): Bird = parrot
