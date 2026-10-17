package spike.compiler.androidx

import com.squareup.kotlinpoet.ClassName
import spike.compiler.conversion.AnnotationNames

object ViewModelNames {
    const val ViewModelEntryPoint = "spike.lifecycle.viewmodel.ViewModelEntryPoint"
    val SpikeViewModel = AnnotationNames("spike.lifecycle.viewmodel.SpikeViewModel")
    val Export = AnnotationNames("spike.Export")
    val IncludeConstructor = AnnotationNames("spike.Include.Constructor")
    val Qualifier = AnnotationNames("spike.Qualifier")
    val ViewModel = ClassName("androidx.lifecycle", "ViewModel")
    val SavedStateHandle = ClassName("androidx.lifecycle", "SavedStateHandle")
    val ViewModelKey = ClassName("spike.lifecycle.viewmodel", "ViewModelKey")
    val SharesSingletons = ClassName("spike", "EntryPoint", "Factory", "SharesSingletons")
    val OptIn = ClassName("kotlin", "OptIn")
    val InternalSpikeApi = ClassName("spike", "InternalSpikeApi")
}
