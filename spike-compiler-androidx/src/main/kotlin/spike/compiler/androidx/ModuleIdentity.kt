package spike.compiler.androidx

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver

@OptIn(KspExperimental::class)
class ModuleIdentity(private val options: Map<String, String>) {
    fun name(resolver: Resolver): String {
        val name = options[ViewModelNames.ModuleOption] ?: resolver.getModuleName().asString()
        return name.map { if (it.isLetterOrDigit()) it else '_' }.joinToString("")
    }
}
