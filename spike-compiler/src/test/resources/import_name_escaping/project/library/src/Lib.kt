package com.example.lib

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Include
class Lib(
    val first: com.example.a.BConfig,
    val second: com.example.aB.Config,
) {
    fun run() = first.value + second.value
}
