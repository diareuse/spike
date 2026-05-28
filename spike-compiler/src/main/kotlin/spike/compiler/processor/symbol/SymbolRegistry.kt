package spike.compiler.processor.symbol

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.EntryPoint
import spike.Export
import spike.Include
import spike.compiler.processor.util.getSymbolsWithAnnotation

@OptIn(KspExperimental::class)
class SymbolRegistry {

    fun spikeViewModel(resolver: Resolver) = resolver
        .getSymbolsWithAnnotation("spike.lifecycle.viewmodel.SpikeViewModel")
        .filterIsInstance<KSClassDeclaration>()

    fun include(resolver: Resolver) = resolver
        .getSymbolsWithAnnotation<Include>()

    fun entryPoint(resolver: Resolver) = resolver
        .getSymbolsWithAnnotation<EntryPoint>()
        .filterIsInstance<KSClassDeclaration>()

    fun export(resolver: Resolver) = resolver
        .getSymbolsWithAnnotation<Export>()
        .filterIsInstance<KSClassDeclaration>()

    fun all(resolver: Resolver) = buildList {
        addAll(spikeViewModel(resolver))
        addAll(include(resolver))
        addAll(entryPoint(resolver))
        addAll(export(resolver))
    }

}