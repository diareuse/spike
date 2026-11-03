package com.example

import com.example.platform.Context

@spike.Include
fun context(): Context = Context()

@spike.Include
class Unused(val context: Context)

@spike.Include
class Screen {
    val name = "screen"
}

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
