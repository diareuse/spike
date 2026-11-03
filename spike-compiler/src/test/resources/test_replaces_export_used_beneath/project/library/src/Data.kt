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
class Session(val id: String, val source: Source)

@spike.Include
class Repository(val sessions: (String) -> Session)

@spike.Include
class Loader(val source: Source)

@spike.Include
class Parent(val child: Child, val source: Source)

@spike.Include
class Child(val parent: spike.Provider<Parent>)

class Plugin(val source: Source)

@spike.Include(bindTo = spike.BindTarget.Set)
fun plugin(source: Source) = Plugin(source)

@spike.Export
interface DataExports {
    val source: Source
    val repository: Repository
    val parent: Parent
    val child: Child

    fun loader(): Loader
}
