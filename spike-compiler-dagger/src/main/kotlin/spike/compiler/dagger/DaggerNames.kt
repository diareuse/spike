package spike.compiler.dagger

import com.squareup.kotlinpoet.ClassName
import spike.compiler.conversion.AnnotationNames

object DaggerNames {
    val Module = AnnotationNames("dagger.Module")
    val Provides = AnnotationNames("dagger.Provides")
    val Binds = AnnotationNames("dagger.Binds")
    val Bindings = AnnotationNames("dagger.Provides", "dagger.Binds")
    val Component = AnnotationNames("dagger.Component")
    val ComponentFactory = AnnotationNames("dagger.Component.Factory")
    val ComponentBuilder = AnnotationNames("dagger.Component.Builder")
    val MapKey = AnnotationNames("dagger.MapKey")
    val Lazy = AnnotationNames("dagger.Lazy")
    val Singleton = AnnotationNames("javax.inject.Singleton", "jakarta.inject.Singleton", "dagger.Reusable")
    val IntoSet = AnnotationNames("dagger.multibindings.IntoSet", "dagger.multibindings.ElementsIntoSet")
    val IntoMap = AnnotationNames("dagger.multibindings.IntoMap")
    val Keys = mapOf(
        "dagger.multibindings.StringKey" to ClassName("spike.dagger", "StringKey"),
        "dagger.multibindings.IntKey" to ClassName("spike.dagger", "IntKey"),
        "dagger.multibindings.LongKey" to ClassName("spike.dagger", "LongKey"),
        "dagger.multibindings.ClassKey" to ClassName("spike.dagger", "ClassKey"),
    )
    val KnownKeys = AnnotationNames(*Keys.keys.toTypedArray())
    val KotlinLazy = ClassName("kotlin", "Lazy")
}
