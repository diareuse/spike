package com.example.library

@spike.EntryPoint
interface DeviceGraph : Graph {
    companion object
}

fun deviceGreeting() = DeviceGraph().greeting.text
