package spike.compiler.conversion

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

class EntryPointTypeSupertype<in T : EntryPointSubject>(private val delegate: EntryPointType<T>) : EntryPointType<T> {
    override fun of(subject: T): TypeSpec {
        val supertype = (subject.declaration as KSClassDeclaration).toClassName()
        return delegate.of(subject).toBuilder().addSuperinterface(supertype).build()
    }
}
