package com.example.app

import com.example.lib.Cat
import com.example.lib.Name
import com.example.lib.Zoo

@spike.EntryPoint
interface Application {
    val zoo: Zoo

    companion object
}

@spike.Include(bindTo = spike.BindTarget.Map)
@Name("tom")
fun appTom() = Cat("app tom")
