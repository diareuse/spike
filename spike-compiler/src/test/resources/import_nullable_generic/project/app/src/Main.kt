package com.example.app

import com.example.lib.Box
import com.example.lib.Config
import com.example.lib.Lib

fun main() {
    val ep: PropertyEntryPoint = PropertyEntryPoint.invoke()
    check(ep.lib.run() == "abc") { "lib produced ${ep.lib.run()}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib

    companion object
}

@spike.Include
fun plain() = Config("a")

@spike.Include
fun maybe(): Config? = Config("b")

@spike.Include
fun box(): Box<String> = Box("c")
