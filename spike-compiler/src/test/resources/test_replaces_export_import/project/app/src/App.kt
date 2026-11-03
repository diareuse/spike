package com.example

import com.example.library.Greeter
import com.example.library.Name

@spike.Include
fun name() = Name("app")

@spike.EntryPoint
interface App {
    val greeter: Greeter

    companion object
}
