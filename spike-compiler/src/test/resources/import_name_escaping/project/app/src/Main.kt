package com.example.app

import com.example.lib.Lib

fun main() {
    val ep: PropertyEntryPoint = PropertyEntryPoint.invoke()
    check(ep.lib.run() == "ab") { "lib produced ${ep.lib.run()}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib

    companion object
}

@spike.Include
fun first() = com.example.a.BConfig("a")

@spike.Include
fun second() = com.example.aB.Config("b")
