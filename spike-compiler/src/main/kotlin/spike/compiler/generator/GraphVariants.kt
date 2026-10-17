package spike.compiler.generator

import spike.Include
import spike.Qualifier

/**
 * The graph generator for app entry points, which builds every binding the entry point needs.
 */
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class AppGraph

/**
 * The graph generator for @Export modules, which publishes bindings and contributions to the app.
 */
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ExportGraph

@Include
@AppGraph
fun appGraph(
    resolver: TypeResolver,
    @AppGraph entryPoint: Generator,
    @AppGraph members: EntryPointMembers,
    dependencyFactory: DependencyFactoryGenerator,
): DependencyGraphGenerator = DependencyGraphGenerator(resolver, entryPoint, members, dependencyFactory)

@Include
@ExportGraph
fun exportGraph(
    resolver: TypeResolver,
    @ExportGraph entryPoint: Generator,
    @ExportGraph members: EntryPointMembers,
    dependencyFactory: DependencyFactoryGenerator,
): DependencyGraphGenerator = DependencyGraphGenerator(resolver, entryPoint, members, dependencyFactory)
