package spike.compiler.koin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import spike.compiler.conversion.AnnotationNames

object KoinNames {
    const val Koin = "org.koin.core.Koin"
    val EntryPoint = AnnotationNames("spike.EntryPoint")
    val FromKoin = AnnotationNames("spike.koin.FromKoin")
    val SpikeQualifier = AnnotationNames("spike.Qualifier")
    val KoinType = ClassName("org.koin.core", "Koin")
    val Module = MemberName("org.koin.dsl", "module")
    val ModuleType = ClassName("org.koin.core.module", "Module")
}
