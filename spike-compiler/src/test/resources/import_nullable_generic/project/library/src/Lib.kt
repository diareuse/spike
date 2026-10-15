package com.example.lib

class Config(val value: String)

class Box<T>(val value: T)

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Include
class Lib(val plain: Config, val maybe: Config?, val box: Box<String>) {
    fun run() = plain.value + maybe?.value + box.value
}
