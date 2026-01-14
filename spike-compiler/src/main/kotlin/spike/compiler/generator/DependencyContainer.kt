package spike.compiler.generator

import com.squareup.kotlinpoet.ClassName
import spike.graph.Type

class DependencyContainer(
    val className: ClassName,
    val lut: Map<Type, String>
) {

    operator fun get(type: Type): String {
        return lut[type]!!
    }

}