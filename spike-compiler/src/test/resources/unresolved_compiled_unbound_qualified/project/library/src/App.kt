package com.example

import com.example.platform.Context

@spike.Qualifier
annotation class Named(val name: String)

@spike.Include
class Screen(@Named("app") val context: Context)

@spike.EntryPoint
interface App {
    val screen: Screen

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(@Named("app") context: Context): App
    }

    companion object
}
