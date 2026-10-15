@spike.EntryPoint
interface Shelter {
    val cat: Cat
    val dog: Dog

    companion object
}

class Cat

@spike.Include
class Dog(val bone: Bone)

class Bone
