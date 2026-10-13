@spike.EntryPoint
interface PropertyEntryPoint {
    val animal: Animal

    companion object
}

interface Animal

@spike.Include(bindAs = Animal::class)
class Tomcat : Animal

@spike.Include(bindAs = Animal::class)
class Bulldog : Animal
