package com.example.test

import com.example.App
import com.example.Foo
import com.example.Screen

@spike.Include
fun foo(screen: Screen): Foo = Foo()

@spike.EntryPoint
interface TestApp : App {
    companion object
}
