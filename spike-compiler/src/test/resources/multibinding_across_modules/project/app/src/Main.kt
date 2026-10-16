package com.example.app

import com.example.lib.Cat
import com.example.lib.Name
import com.example.lib.Zoo

fun main() {
    val app = Application()
    val zoo = app.zoo
    check(zoo.cats.map { it.name }.sorted() == listOf("app", "library")) { "cats ${zoo.cats}" }
    check(zoo.unique.map { it.name }.toSet() == setOf("app unique", "library unique")) { "unique ${zoo.unique}" }
    check(zoo.named.mapValues { it.value.name } == mapOf("app" to "app named", "library" to "library named")) {
        "named ${zoo.named}"
    }
    check(app.cats.map { it.name }.sorted() == listOf("app", "library")) { "app cats ${app.cats}" }
}

@spike.EntryPoint
interface Application {
    val zoo: Zoo
    val cats: List<Cat>

    companion object
}

@spike.Include(bindTo = spike.BindTarget.List)
fun appCat() = Cat("app")

@spike.Include(bindTo = spike.BindTarget.Set)
fun appUnique() = Cat("app unique")

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("app")
fun appNamed() = Cat("app named")
