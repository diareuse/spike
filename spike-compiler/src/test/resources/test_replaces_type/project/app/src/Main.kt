package com.example.test

import com.example.App
import com.example.Repo

@spike.Include
@spike.test.Replaces(Repo::class)
class NotARepo

@spike.EntryPoint
interface TestApp : App {
    companion object
}
