package com.example.library

class Name(val value: String)

@spike.Include
class Greeter(private val name: Name) {
    fun greet() = "hello ${name.value}"
}

@spike.Export
interface GreeterExports {
    val greeter: Greeter
}
