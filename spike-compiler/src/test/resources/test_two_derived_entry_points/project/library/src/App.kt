package com.example

interface Repo {
    fun name(): String
}

@spike.Include
class RealRepo : Repo {
    override fun name() = "real"
}

@spike.Include
val RealRepo.asRepo: Repo get() = this

@spike.Include
class Screen(val repo: Repo)

@spike.EntryPoint
interface App {
    val screen: Screen

    companion object
}
