package com.example

import com.example.platform.Context

class Repo<T>

@spike.Include
class Screen(val repo: Repo<Context>)

@spike.EntryPoint
interface App {
    val screen: Screen

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(repo: Repo<Context>): App
    }

    companion object
}
