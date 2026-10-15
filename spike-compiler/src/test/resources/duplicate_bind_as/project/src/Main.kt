@spike.EntryPoint
interface PropertyEntryPoint {
    val animal: Animal

    companion object
}

interface Animal

@spike.Include
class Tomcat : Animal

@spike.Include
val Tomcat.asAnimal: Animal get() = this

@spike.Include
class Bulldog : Animal

@spike.Include
val Bulldog.asAnimal: Animal get() = this
