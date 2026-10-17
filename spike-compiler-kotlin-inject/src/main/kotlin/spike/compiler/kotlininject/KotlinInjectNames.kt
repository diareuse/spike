package spike.compiler.kotlininject

import spike.compiler.conversion.AnnotationNames

object KotlinInjectNames {
    val Inject = AnnotationNames("me.tatarka.inject.annotations.Inject")
    val Provides = AnnotationNames("me.tatarka.inject.annotations.Provides")
    val Component = AnnotationNames("me.tatarka.inject.annotations.Component")
    val IntoSet = AnnotationNames("me.tatarka.inject.annotations.IntoSet")
    val IntoMap = AnnotationNames("me.tatarka.inject.annotations.IntoMap")
    val Scope = AnnotationNames("me.tatarka.inject.annotations.Scope")
    val Qualifier = AnnotationNames("me.tatarka.inject.annotations.Qualifier")
}
