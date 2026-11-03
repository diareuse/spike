package com.example.library

class Name(val value: String)

@spike.Export
interface GreeterExport {
    val greeter: Greeter
}

@spike.Include
class Greeter(private val name: Name) {
    fun greet() = "hello ${name.value}"
}
