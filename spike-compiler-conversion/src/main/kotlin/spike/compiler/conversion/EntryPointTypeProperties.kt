package spike.compiler.conversion

import com.google.devtools.ksp.isAbstract
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * Copies the abstract properties of a component, other than extensions and those annotated with one of [excluded],
 * to its entry point.
 */
class EntryPointTypeProperties<in T : EntryPointSubject>(
    private val excluded: AnnotationNames,
    private val delegate: EntryPointType<T>,
) : EntryPointType<T> {
    override fun of(subject: T): TypeSpec {
        val properties = subject.declaration.getAllProperties()
            .filter { it.isAbstract() && it.extensionReceiver == null && !excluded.annotates(it) }
            .map { PropertySpec.builder(it.simpleName.asString(), it.type.toTypeName()).build() }
        return delegate.of(subject).toBuilder().addProperties(properties.asIterable()).build()
    }
}
