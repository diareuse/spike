package com.example.library

@spike.EntryPoint
interface DomainGraph : Graph {
    companion object
}

fun domainGreeting() = DomainGraph().greeting.text
