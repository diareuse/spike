package spike.compiler.conversion

import com.squareup.kotlinpoet.TypeSpec

class EntryPointTypeInterface : EntryPointType<EntryPointSubject> {
    override fun of(subject: EntryPointSubject): TypeSpec = TypeSpec.interfaceBuilder(subject.entryPoint)
        .addAnnotation(SpikeNames.EntryPoint)
        .addType(TypeSpec.companionObjectBuilder().build())
        .build()
}
