package spike.compiler.jsr

import com.squareup.kotlinpoet.ClassName
import spike.compiler.conversion.AnnotationNames

object JsrNames {
    val Inject = AnnotationNames("javax.inject.Inject", "jakarta.inject.Inject")
    val Singleton = AnnotationNames("javax.inject.Singleton", "jakarta.inject.Singleton")
    val Named = AnnotationNames("javax.inject.Named", "jakarta.inject.Named")
    val Qualifier = AnnotationNames("javax.inject.Qualifier", "jakarta.inject.Qualifier")
    val Scope = AnnotationNames("javax.inject.Scope", "jakarta.inject.Scope")
    val Provider = AnnotationNames("javax.inject.Provider", "jakarta.inject.Provider")
    val SpikeNamed = ClassName("spike.jsr", "Named")
}
