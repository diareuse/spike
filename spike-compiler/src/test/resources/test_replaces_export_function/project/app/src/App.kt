package com.example

import com.example.data.Clock
import com.example.data.Source

@spike.Include
class Screen(val source: Source, val clock: Clock)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
