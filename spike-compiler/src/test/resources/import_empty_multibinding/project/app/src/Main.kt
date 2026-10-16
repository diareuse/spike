package com.example.app

import com.example.lib.Lib

fun main() {
    val lib = PropertyEntryPoint().lib
    check(lib.run() == "a, b|1|enabled=true") { "lib produced ${lib.run()}" }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val lib: Lib

    companion object
}

@spike.Key
annotation class Flag(val name: String)

@spike.Include
fun names(): List<String> = listOf("a", "b")

@spike.Include(bindTo = spike.BindTarget.Set)
fun one(): Int = 1

@spike.Include(bindTo = spike.BindTarget.Map)
@Flag("enabled")
fun enabled(): Boolean = true
