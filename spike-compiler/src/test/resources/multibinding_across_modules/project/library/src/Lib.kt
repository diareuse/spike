package com.example.lib

data class Cat(val name: String)

@spike.Key
annotation class Name(val value: String)

@spike.Qualifier
annotation class Wild

@spike.Export
interface Exported {
    val zoo: Zoo
}

@spike.Export
interface Other {
    val keeper: Keeper
}

@spike.Include
class Keeper

@spike.Include
class Zoo(val cats: List<Cat>, val unique: Set<Cat>, val named: Map<String, Cat>)

@spike.Include(bindTo = spike.BindTarget.List)
fun libraryCat() = Cat("library")

@spike.Include(bindTo = spike.BindTarget.Set)
fun libraryUnique() = Cat("library unique")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("library")
fun libraryNamed() = Cat("library named")

@spike.Include(bindTo = spike.BindTarget.List)
@Wild
fun wildCat() = Cat("wild")
