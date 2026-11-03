package com.example.app

import com.example.feature.Suffix
import com.example.feature.Welcome
import com.example.library.Name

fun main() {
    val shown = AppEntryPoint().welcome.show()
    check(shown == "hello app, welcome") { "welcome showed $shown" }
}

@spike.EntryPoint
interface AppEntryPoint {
    val welcome: Welcome

    companion object
}

@spike.Include
fun name() = Name("app")

@spike.Include
fun suffix() = Suffix("welcome")
