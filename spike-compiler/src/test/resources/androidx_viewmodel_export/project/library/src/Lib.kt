package com.example.lib

@spike.Export
interface Exported {
    val lib: Lib
}

@spike.Singleton
@spike.Include
class Lib
