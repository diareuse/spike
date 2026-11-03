package com.example

import com.example.platform.Context

class Plugin(val name: String)

@spike.Key
annotation class Named(val name: String)

@spike.Include
fun context(): Context = Context()

@spike.Include(bindTo = spike.BindTarget.Set)
fun entry(context: Context) = Plugin("entry")

@spike.Include(bindTo = spike.BindTarget.Set)
fun spread(context: Context): Set<Plugin> = setOf(Plugin("spread"))

@spike.Include(bindTo = spike.BindTarget.Map)
@Named("mapped")
fun mapped(context: Context) = Plugin("mapped")

@spike.Include
class Provided(val context: Context)

@spike.Include
class Lazily(val context: Context)

@spike.Include
class Detail(val id: String, val context: Context)

@spike.EntryPoint
interface App {
    val plugins: Set<Plugin>
    val named: Map<String, Plugin>
    val provided: spike.Provider<Provided>
    val lazily: Lazy<Lazily>
    val detail: (String) -> Detail

    companion object
}
