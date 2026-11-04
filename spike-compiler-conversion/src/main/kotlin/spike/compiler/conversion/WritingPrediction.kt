package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver

interface WritingPrediction {
    val provider: String

    fun writes(resolver: Resolver): Boolean
}
