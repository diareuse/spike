package spike.compiler

import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import spike.compiler.processor.SpikeSymbolProcessor

class SpikeSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SpikeSymbolProcessor(environment)
    }
}

fun PropertySpec.Companion.overriding(property: KSPropertyDeclaration): PropertySpec.Builder {
    return PropertySpec.builder(property.simpleName.asString(), property.type.resolve().toTypeName())
        .addModifiers(KModifier.OVERRIDE)
}

fun FunSpec.Companion.overriding(function: KSFunctionDeclaration): FunSpec.Builder {
    return when {
        function.isConstructor() -> FunSpec.constructorBuilder()
        else -> FunSpec.builder(function.simpleName.asString()).addModifiers(KModifier.OVERRIDE)
    }.apply {
        function.extensionReceiver?.let { receiver(it.toTypeName()) }
        function.returnType?.let { returns(it.toTypeName()) }
        for (param in function.parameters) {
            addParameter(param.toParameterSpec())
        }
    }
}

fun KSValueParameter.toParameterSpec(): ParameterSpec {
    return ParameterSpec(name!!.asString(), type.resolve().toClassName())
}