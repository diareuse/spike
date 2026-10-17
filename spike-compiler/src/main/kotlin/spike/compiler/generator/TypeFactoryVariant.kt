package spike.compiler.generator

import spike.Key
import spike.compiler.graph.TypeFactory
import kotlin.reflect.KClass

/**
 * Keys a [TypeFactoryCode] by the variant of [TypeFactory] it writes.
 */
@Key
@Retention(AnnotationRetention.SOURCE)
annotation class TypeFactoryVariant(val value: KClass<out TypeFactory>)
