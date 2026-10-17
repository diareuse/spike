package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSDeclaration

/**
 * A subject of conversion, read through the declaration whose annotations and file describe it.
 */
interface Declared {
    val declaration: KSDeclaration
}
