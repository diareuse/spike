package com.example.library

@spike.EntryPoint
interface UnitGraph : Graph {
    companion object
}

fun unitGreeting() = UnitGraph().greeting.text
