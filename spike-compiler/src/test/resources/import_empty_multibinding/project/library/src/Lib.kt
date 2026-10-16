package com.example.lib

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Include
class Lib(val names: List<String>, val ids: Set<Int>, val flags: Map<String, Boolean>) {
    fun run() = "${names.joinToString()}|${ids.joinToString()}|${flags.entries.joinToString()}"
}
