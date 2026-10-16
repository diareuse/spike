package com.example.lib

class Config(val value: String)

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Export
interface Other {
    val other: OtherLib
}

@spike.Include
class Lib(val config: Config) {
    fun run() = config.value
}

@spike.Include
class OtherLib(val config: Config) {
    fun run() = config.value
}
