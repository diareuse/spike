package com.example.test

import com.example.App
import com.example.invoke
import com.example.library.Name

fun main() {
    check(TestApp().greeter.greet() == "hello test") { "test greeted ${TestApp().greeter.greet()}" }
    check(App().greeter.greet() == "hello app") { "production greeted ${App().greeter.greet()}" }
}

@spike.Include
@spike.test.Replaces(Name::class)
fun testName() = Name("test")

@spike.EntryPoint
interface TestApp : App {
    companion object
}
