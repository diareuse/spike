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
fun DependencyGraphGenerator.Factory.appGraph(): DependencyGraphGenerator = create(false)

@Include
@ExportGraph
fun DependencyGraphGenerator.Factory.exportGraph(): DependencyGraphGenerator = create(true)
