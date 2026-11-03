package spike.compiler.processor.symbol

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import spike.Include
import spike.Key
import spike.Qualifier

@Include
@OptIn(KspExperimental::class)
class CompiledSignatureTypesSpike(private val signatures: SignatureTypes) : CompiledSignatureTypes {
    override fun of(symbol: KSAnnotated): Sequence<SignatureType> = signatures.of(symbol).filter { it.readBySpike() }

    private fun SignatureType.readBySpike(): Boolean {
        if (node.parent is KSAnnotation) return false
        val annotation = node as? KSAnnotation ?: return true
        val declaration = annotation.annotationType.resolve().declaration
        return declaration.packageName.asString() in SpikePackages ||
            declaration.isAnnotationPresent(Key::class) ||
            declaration.isAnnotationPresent(Qualifier::class)
    }

    private companion object {
        val SpikePackages = setOf("spike", "spike.test")
    }
}
