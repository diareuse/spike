package spike.compiler.graph

import spike.Include

/**
 * Splits a multibinding with more than [size] entries into a concatenation of smaller ones, so that no generated
 * method that builds it grows past what the JIT compiles.
 */
class MultibindingSegments(private val size: Int) {

    fun of(map: TypeFactory.MultibindsMap): TypeFactory {
        if (map.keyValues.size <= size) return map
        val parts = map.keyValues.entries.chunked(size).map { chunk ->
            TypeFactory.MultibindsMap(map.type, chunk.associate { it.key to it.value })
        }
        return TypeFactory.Concatenation(map.type, parts)
    }

    fun of(collection: TypeFactory.MultibindsCollection): TypeFactory {
        if (collection.entries.size <= size) return collection
        val parts = collection.entries.chunked(size).map { chunk ->
            TypeFactory.MultibindsCollection(collection.type, chunk, collection.collectionType)
        }
        return TypeFactory.Concatenation(collection.type, parts)
    }

    companion object {
        private const val SIZE = 100

        @Include
        fun default(): MultibindingSegments = MultibindingSegments(SIZE)
    }
}
