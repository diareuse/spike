fun main() {
    require(Shelter().bird is Parrot)
}

@spike.EntryPoint
interface Shelter {
    val bird: Bird

    companion object
}

interface Bird {
    companion object {
        @spike.Include
        fun bind(parrot: Parrot): Bird = parrot
    }
}

@spike.Include
class Parrot : Bird
