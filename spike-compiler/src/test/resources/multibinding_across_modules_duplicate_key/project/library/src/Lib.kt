package com.example.lib

data class Cat(val name: String)

@spike.Key
annotation class Name(val value: String)

@spike.Export
interface Exported {
    val zoo: Zoo
}

@spike.Include
class Zoo(val named: Map<String, Cat>)

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("tom")
fun libraryTom() = Cat("library tom")
