package com.example.lib

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Include
class Lib(val first: com.example.a.Config, val second: com.example.b.Config) {
    fun run() = first.value + second.value
}
