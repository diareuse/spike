package com.example.test

import com.example.App
import com.example.Source

@spike.Include
@spike.test.Replaces(Source::class)
class FakeSource : Source {
    override fun name() = "fake"
}

@spike.EntryPoint
interface TestApp : App {
    companion object
}
