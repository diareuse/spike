package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSNode

data class ConversionProblem(val code: String, val message: String, val node: KSNode)
