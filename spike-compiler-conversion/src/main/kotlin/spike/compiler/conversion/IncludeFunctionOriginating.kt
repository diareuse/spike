package spike.compiler.conversion

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile

class IncludeFunctionOriginating<in T : Declared>(private val delegate: IncludeFunction<T>) : IncludeFunction<T> {
    override fun of(subject: T): FunSpec {
        val function = delegate.of(subject)
        val file = subject.declaration.containingFile ?: return function
        return function.toBuilder().addOriginatingKSFile(file).build()
    }
}
