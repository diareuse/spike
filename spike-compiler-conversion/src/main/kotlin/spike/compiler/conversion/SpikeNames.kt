package spike.compiler.conversion

import com.squareup.kotlinpoet.ClassName

object SpikeNames {
    val Include = ClassName("spike", "Include")
    val Singleton = ClassName("spike", "Singleton")
    val Provider = ClassName("spike", "Provider")
    val EntryPoint = ClassName("spike", "EntryPoint")
    val EntryPointFactory = ClassName("spike", "EntryPoint", "Factory")
    val BindTarget = ClassName("spike", "BindTarget")
}
