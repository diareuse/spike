package com.example.app

import com.example.lib.Lib
import com.example.lib.OtherLib

fun main() {
    val ep: PropertyEntryPoint = PropertyEntryPoint.invoke()
    check(ep.lib.run() == "a") { "lib produced ${ep.lib.run()}" }
    check(ep.other.run() == "a") { "other produced ${ep.other.run()}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib
    val other: OtherLib

    companion object
}

@spike.Include
fun config() = com.example.a.Config("a")
