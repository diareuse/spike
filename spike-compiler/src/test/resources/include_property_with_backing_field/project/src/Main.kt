@spike.EntryPoint
interface Shelter {
    val clock: Clock

    companion object
}

class Clock

@spike.Include
val clock = Clock()
