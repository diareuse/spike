package spike.compiler.metro

import com.squareup.kotlinpoet.ClassName
import spike.compiler.conversion.AnnotationNames

object MetroNames {
    private const val Package = "dev.zacsweers.metro"
    val Injected = AnnotationNames("$Package.Inject", "$Package.AssistedInject")
    val AssistedFactory = AnnotationNames("$Package.AssistedFactory")
    val Provides = AnnotationNames("$Package.Provides")
    val Binds = AnnotationNames("$Package.Binds")
    val Contributes = AnnotationNames("$Package.ContributesBinding", "$Package.ContributesIntoSet")
    val ContributesIntoSet = AnnotationNames("$Package.ContributesIntoSet")
    val DependencyGraph = AnnotationNames("$Package.DependencyGraph")
    val DependencyGraphFactory = AnnotationNames("$Package.DependencyGraph.Factory")
    val GraphExtension = AnnotationNames("$Package.GraphExtension")
    val Includes = AnnotationNames("$Package.Includes")
    val SingleIn = AnnotationNames("$Package.SingleIn")
    val Scope = AnnotationNames("$Package.Scope")
    val Named = AnnotationNames("$Package.Named")
    val Qualifier = AnnotationNames("$Package.Qualifier")
    val IntoSet = AnnotationNames("$Package.IntoSet", "$Package.ElementsIntoSet")
    val IntoMap = AnnotationNames("$Package.IntoMap")
    val Provider = AnnotationNames("$Package.Provider")
    val Unconvertible = AnnotationNames("$Package.OptionalBinding", "$Package.DefaultBinding")
    val Keys = mapOf(
        "$Package.StringKey" to ClassName("spike.metro", "StringKey"),
        "$Package.IntKey" to ClassName("spike.metro", "IntKey"),
        "$Package.ClassKey" to ClassName("spike.metro", "ClassKey"),
    )
    val SpikeNamed = ClassName("spike.metro", "Named")
}
