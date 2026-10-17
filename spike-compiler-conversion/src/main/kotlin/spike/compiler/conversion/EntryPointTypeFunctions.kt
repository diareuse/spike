package spike.compiler.conversion

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * Copies the abstract functions without parameters of a component, other than extensions and those annotated with
 * one of [excluded], to its entry point.
 */
class EntryPointTypeFunctions<in T : EntryPointSubject>(
    private val excluded: AnnotationNames,
    private val delegate: EntryPointType<T>,
) : EntryPointType<T> {
    override fun of(subject: T): TypeSpec {
        val functions = subject.declaration.getAllFunctions()
            .filter { it.isAbstract && it.parameters.isEmpty() && it.extensionReceiver == null }
            .filterNot(excluded::annotates)
            .map { function ->
                FunSpec.builder(function.simpleName.asString())
                    .addModifiers(KModifier.ABSTRACT)
                    .returns(function.returnType!!.toTypeName())
                    .build()
            }
        return delegate.of(subject).toBuilder().addFunctions(functions.asIterable()).build()
    }
}
