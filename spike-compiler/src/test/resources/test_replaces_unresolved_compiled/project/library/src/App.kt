package com.example

import com.example.platform.Context

interface Source {
    fun name(): String
}

@spike.Include
class RemoteSource : Source {
    override fun name() = "remote"
}

@spike.Include
val RemoteSource.asSource: Source get() = this

@spike.Include
fun context(): Context = Context()

@spike.Include
class Screen(val source: Source, val context: Context)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
