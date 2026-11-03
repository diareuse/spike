package com.example

import com.example.platform.Context

@spike.Include
fun context(): Context = Context()

@spike.Include
class Screen(val context: Context)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
