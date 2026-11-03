package com.example

import com.example.platform.Context

interface Screen {
    fun name(): String
}

@spike.Include
fun context(): Context = Context()

@spike.Include
class RealScreen(val context: Context) : Screen {
    override fun name() = "real"
}

@spike.Include
val RealScreen.asScreen: Screen get() = this

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
