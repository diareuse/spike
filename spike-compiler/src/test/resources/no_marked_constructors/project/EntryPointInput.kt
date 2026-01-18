@spike.EntryPoint
interface CatEP {
    val cat: Cat

    companion object
}

@spike.Include
class Cat(
    val one: Life,
    val two: Life
) {

    constructor(one: Life, two: Life, three: Life): this(one, two)

}

@spike.Include
class Life