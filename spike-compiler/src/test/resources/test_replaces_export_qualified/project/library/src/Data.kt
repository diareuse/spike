package com.example.data

interface Source {
    fun name(): String
}

@spike.Qualifier
annotation class Primary

@spike.Qualifier
annotation class Secondary

@spike.Include
@Primary
fun primarySource(): Source = object : Source {
    override fun name() = "primary"
}

@spike.Include
@Secondary
fun secondarySource(): Source = object : Source {
    override fun name() = "secondary"
}

@spike.Export
interface DataExports {
    @Primary
    val primary: Source

    @Secondary
    val secondary: Source
}
