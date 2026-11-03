package com.example.data

interface Source {
    fun name(): String
}

@spike.Include
class RemoteSource : Source {
    override fun name() = "remote"
}

@spike.Include
val RemoteSource.asSource: Source get() = this

@spike.Include
class Repository(val source: Source)

@spike.Export
interface DataExports {
    val source: Source
    val repository: Repository
}
