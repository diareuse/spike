package spike.compiler.conversion

import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile

class EntryPointTypeOriginating<in T : EntryPointSubject>(
    private val delegate: EntryPointType<T>,
) : EntryPointType<T> {
    override fun of(subject: T): TypeSpec {
        val type = delegate.of(subject)
        val file = subject.declaration.containingFile ?: return type
        return type.toBuilder().addOriginatingKSFile(file).build()
    }
}
