package spike.compiler.koin

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

/**
 * Writes the @Include function that takes [type] from a Koin instance, originating from [entryPoint].
 */
fun interface KoinImport {
    fun write(type: KSType, entryPoint: KSClassDeclaration)
}
