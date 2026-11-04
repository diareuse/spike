package spike.compiler.conversion

import com.google.devtools.ksp.processing.Resolver

/**
 * Asked in round 1, where the resolver shows only dirty files in an incremental build, so [writes] must not depend on symbols.
 */
interface WritingPrediction {
    val provider: String

    fun writes(resolver: Resolver): Boolean
}
