package com.example.lib

@spike.Export
interface Exported {
    val lib: Lib
    val other: OtherLib
}

@spike.Include
class Lib(val config: com.example.a.Config) {
    fun run() = config.value
}

@spike.Include
class OtherLib(val config: com.example.a.Config) {
    fun run() = config.value
}
