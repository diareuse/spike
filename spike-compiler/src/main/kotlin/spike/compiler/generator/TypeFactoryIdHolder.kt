package spike.compiler.generator

import spike.compiler.graph.TypeFactory

class TypeFactoryIdHolder {
    private val ids = HashMap<TypeFactory, Int>()

    fun getOrAdd(typeFactory: TypeFactory): Int = ids.getOrPut(typeFactory) { ids.size }
}
