package com.example

import com.example.platform.Context

@spike.Include
class Lib(val context: Context)

@spike.Export
interface Exported {
    fun lib(): Lib
}
