package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName

/**
 * A component of another framework, replaced by the Spike entry point named [entryPoint].
 */
interface EntryPointSubject : Declared {
    override val declaration: KSClassDeclaration
    val entryPoint: ClassName
}
