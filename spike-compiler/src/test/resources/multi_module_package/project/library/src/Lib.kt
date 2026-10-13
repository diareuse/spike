package com.example.lib

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Include
class Lib(val prefix: String) {
    fun run() = "${prefix}rary"
}
