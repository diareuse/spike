package com.example

import com.example.data.Repository
import com.example.data.Source

@spike.Include
class Screen(val source: Source, val repository: Repository)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
