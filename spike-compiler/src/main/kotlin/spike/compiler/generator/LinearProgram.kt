package spike.compiler.generator

import spike.compiler.graph.TypeFactory

data class LinearStep(val factory: TypeFactory, val arguments: List<Int>)

data class LinearProgram(val steps: List<LinearStep>, val takesArguments: Boolean = false)
