package com.example.app

fun main() {
    check(Helper().name == "helper")
}

@spike.Include
class Helper {
    val name = "helper"
}
