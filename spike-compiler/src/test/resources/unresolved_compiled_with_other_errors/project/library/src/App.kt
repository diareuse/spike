package com.example

import com.example.platform.Context

class Foo

@spike.Include
class Screen(val context: Context, val foo: Foo)

@spike.EntryPoint
interface App {
    val screen: Screen

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(context: Context, foo: Foo): App
    }

    companion object
}
