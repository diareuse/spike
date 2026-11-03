package com.example

import com.example.data.Source

@spike.Include
class Screen(val source: Source)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
