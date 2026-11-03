package com.example

import com.example.platform.Reason
import kotlin.reflect.KClass

@spike.Key
annotation class ClassKey(val value: KClass<*>)

class Cat

@spike.Include(bindTo = spike.BindTarget.Map)
@ClassKey(Reason::class)
fun cat(): Cat = Cat()

@spike.EntryPoint
interface App {
    val cats: Map<KClass<*>, Cat>

    companion object
}
