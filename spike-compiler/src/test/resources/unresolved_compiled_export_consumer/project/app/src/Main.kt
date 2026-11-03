package com.example.app

import com.example.Lib

@spike.Include
class Screen(val lib: Lib)

@spike.Export
interface Screens {
    fun screen(): Screen
}
