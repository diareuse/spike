package com.example

import com.example.platform.Context

@spike.Include
class Screen(val context: Context)

@spike.EntryPoint
interface App {
    val screen: Screen

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(context: Context): App
    }

    companion object
}
