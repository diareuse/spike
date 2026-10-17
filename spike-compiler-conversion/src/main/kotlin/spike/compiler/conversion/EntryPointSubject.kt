package spike.compiler.conversion

import com.squareup.kotlinpoet.ClassName

/**
 * A component of another framework, replaced by the Spike entry point named [entryPoint].
 */
interface EntryPointSubject : Declared {
    val entryPoint: ClassName
}
