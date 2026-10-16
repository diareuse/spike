package spike.compiler.graph

import spike.BindTarget

/**
 * What a graph is built for: an app resolves everything it needs, while an @Export module leaves multibindings
 * and unbound types to the app that imports it and exports its contributions instead.
 */
sealed interface GraphRole {
    fun creators(
        resolving: List<TypeFactoryCreator>,
        multibinding: MultiBindingStore,
        imported: ImportedContributions,
    ): List<TypeFactoryCreator>

    fun contributions(export: () -> List<ExportedContribution>): List<ExportedContribution>

    /**
     * The imports that are errors in this graph, because nothing will provide them later.
     */
    fun unresolved(imports: List<TypeFactory.Imported>): List<TypeFactory.Imported>

    data object App : GraphRole {
        override fun creators(
            resolving: List<TypeFactoryCreator>,
            multibinding: MultiBindingStore,
            imported: ImportedContributions,
        ) = resolving + listOf(
            TypeFactoryCreatorMultiBindMap(multibinding, imported),
            TypeFactoryCreatorMultiBindCollection(multibinding.set, imported, BuiltInTypes.Set, BindTarget.Set),
            TypeFactoryCreatorMultiBindCollection(multibinding.list, imported, BuiltInTypes.List, BindTarget.List),
        )

        override fun contributions(export: () -> List<ExportedContribution>) = emptyList<ExportedContribution>()

        override fun unresolved(imports: List<TypeFactory.Imported>) = imports
    }

    data object Module : GraphRole {
        override fun creators(
            resolving: List<TypeFactoryCreator>,
            multibinding: MultiBindingStore,
            imported: ImportedContributions,
        ) = resolving

        override fun contributions(export: () -> List<ExportedContribution>) = export()

        override fun unresolved(imports: List<TypeFactory.Imported>) = emptyList<TypeFactory.Imported>()
    }
}
