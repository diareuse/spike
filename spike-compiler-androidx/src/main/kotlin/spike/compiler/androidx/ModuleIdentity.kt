package spike.compiler.androidx

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver

@OptIn(KspExperimental::class)
class ModuleIdentity(private val options: Map<String, String>) {
    fun name(resolver: Resolver): String =
        ModuleName.encode(options[ViewModelNames.ModuleOption] ?: resolver.getModuleName().asString())
}
