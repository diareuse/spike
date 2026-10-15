@spike.EntryPoint
interface First {
    val cat: Cat
}

@spike.EntryPoint
interface Second {
    val cat: Cat
}

@spike.Include
class Cat
