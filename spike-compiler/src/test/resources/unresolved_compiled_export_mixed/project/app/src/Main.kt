package com.example.app

import com.example.Lib

@spike.EntryPoint
interface App {
    val lib: Lib
    val name: String

    companion object
}

@spike.Include
class Screen(val lib: Lib)

@spike.Export
interface Screens {
    fun screen(): Screen
}
