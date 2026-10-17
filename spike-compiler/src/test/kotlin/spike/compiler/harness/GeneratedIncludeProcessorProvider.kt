package spike.compiler.harness

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class GeneratedIncludeProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = object : SymbolProcessor {
        private var generated = false

        override fun process(resolver: Resolver): List<KSAnnotated> {
            if (generated) return emptyList()
            generated = true
            val classes = resolver.getSymbolsWithAnnotation("GeneratesInclude").filterIsInstance<KSClassDeclaration>()
            for (declaration in classes) {
                val name = declaration.simpleName.asString()
                val file = declaration.containingFile ?: continue
                environment.codeGenerator.createNewFile(Dependencies(false, file), "", "${name}Include").use {
                    it.write("@spike.Include\nfun generated$name(): $name = $name()\n".toByteArray())
                }
            }
            return emptyList()
        }
    }
}
