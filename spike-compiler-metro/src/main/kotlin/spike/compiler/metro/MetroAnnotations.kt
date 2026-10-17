package spike.compiler.metro

import spike.compiler.conversion.Declared
import spike.compiler.conversion.IncludeFunctionBindTo
import spike.compiler.conversion.IncludeFunctionDecorator
import spike.compiler.conversion.IncludeFunctionMapKey
import spike.compiler.conversion.IncludeFunctionNamed
import spike.compiler.conversion.IncludeFunctionOriginating
import spike.compiler.conversion.IncludeFunctionScoped
import spike.compiler.conversion.NameLookup
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.SpikeNames

/**
 * The annotation decorators that every Metro declaration converted to an @Include function shares, for any subject.
 */
internal object MetroAnnotations {
    fun <T : Declared> intoMap() = IncludeFunctionDecorator<T> { IncludeFunctionBindTo(MetroNames.IntoMap, "Map", it) }

    fun <T : Declared> intoSet() = IncludeFunctionDecorator<T> { IncludeFunctionBindTo(MetroNames.IntoSet, "Set", it) }

    fun <T : Declared> scoped(scopes: ScopeLookup) =
        IncludeFunctionDecorator<T> { IncludeFunctionScoped(scopes, SpikeNames.Singleton, it) }

    fun <T : Declared> named(names: NameLookup) =
        IncludeFunctionDecorator<T> { IncludeFunctionNamed(names, MetroNames.SpikeNamed, it) }

    fun <T : Declared> mapKey() = IncludeFunctionDecorator<T> { IncludeFunctionMapKey(MetroNames.Keys, it) }

    fun <T : Declared> originating() = IncludeFunctionDecorator<T> { IncludeFunctionOriginating(it) }
}
