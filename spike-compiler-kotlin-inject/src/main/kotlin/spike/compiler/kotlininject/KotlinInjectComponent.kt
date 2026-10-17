package spike.compiler.kotlininject

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.EntryPointSubject

class KotlinInjectComponent(override val declaration: KSClassDeclaration) : EntryPointSubject {
    val inputs get() = declaration.primaryConstructor?.parameters.orEmpty()

    override val entryPoint: ClassName
        get() {
            val name = declaration.toClassName()
            return ClassName(name.packageName, name.simpleNames.joinToString("") + "Spike")
        }

    fun isInput(property: KSPropertyDeclaration) = inputs.any { it.name?.asString() == property.simpleName.asString() }

    companion object {
        fun of(symbol: KSAnnotated) = (symbol as? KSClassDeclaration)?.let(::KotlinInjectComponent)

        /**
         * Tells a component's constructor property apart from a @Provides that kotlin-inject reads from it.
         */
        fun isInput(symbol: KSAnnotated): Boolean {
            val property = symbol as? KSPropertyDeclaration
            val component = (property?.parentDeclaration as? KSClassDeclaration)
                ?.takeIf(KotlinInjectNames.Component::annotates) ?: return false
            return KotlinInjectComponent(component).isInput(property)
        }
    }
}
