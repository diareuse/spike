package spike.compiler.generator

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import spike.compiler.graph.GraphEntryPoint
import spike.compiler.graph.SingletonOwnership
import spike.factory.SingletonHolder

sealed interface EntryPointSingletonOwnership {
    fun addMembers(factory: TypeSpec.Builder)
    fun dependencyFactoryArguments(inputs: List<String>): List<String>

    data object PerEntryPoint : EntryPointSingletonOwnership {
        override fun addMembers(factory: TypeSpec.Builder) = Unit
        override fun dependencyFactoryArguments(inputs: List<String>) = inputs
    }

    data object SharedByFactory : EntryPointSingletonOwnership {
        override fun addMembers(factory: TypeSpec.Builder) {
            factory.addProperty(
                PropertySpec.builder("singletons", SingletonHolder::class, KModifier.PRIVATE)
                    .initializer("%T()", SingletonHolder::class)
                    .build()
            )
        }

        override fun dependencyFactoryArguments(inputs: List<String>) = inputs + "singletons = singletons"
    }

    companion object {
        private val byOwnership: Map<SingletonOwnership, EntryPointSingletonOwnership> = mapOf(
            SingletonOwnership.PerEntryPoint to PerEntryPoint,
            SingletonOwnership.SharedByFactory to SharedByFactory,
        )

        fun of(factory: GraphEntryPoint.Factory): EntryPointSingletonOwnership = byOwnership.getValue(factory.ownership)
    }
}
