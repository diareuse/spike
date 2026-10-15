@spike.Export
interface Base {
    val lib: Lib
}

@spike.Export
interface Derived : Base

@spike.Include
class Lib
