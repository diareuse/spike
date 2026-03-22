package spike.factory

public abstract class DependencyFactory {

    protected abstract val maxConstructorArgs: Int

    public inline fun <reified T> get(id: DependencyId): T {
        return initialize(id) as T
    }

    // ---

    @PublishedApi
    internal fun initialize(rootId: DependencyId): Any {
        // Fetch the flattened instruction set for this specific root dependency
        val instructions = getInstructions(rootId)
            ?: return instantiate(emptyArray(), rootId)

        // instructions[0] contains the exact number of instances required for this graph
        val contextSize = instructions[0]
        val context = arrayOfNulls<Any>(contextSize)
        val argBuffer = arrayOfNulls<Any>(maxConstructorArgs)

        var programCounter = 1
        var contextIndex = 0

        while (programCounter < instructions.size) {
            val depId = instructions[programCounter++]
            val argCount = instructions[programCounter++]

            // Populate the shared buffer using relative pointers into our local context
            for (i in 0 until argCount) {
                val sourceIndex = instructions[programCounter++]
                argBuffer[i] = context[sourceIndex]
            }

            context[contextIndex++] = instantiate(argBuffer, DependencyId(depId))

            // Note: We deliberately DO NOT clear the argBuffer here.
            // The generated factories only read up to their specific parameter count,
            // and the entire argBuffer is garbage collected the moment this function returns.
        }

        // The root object is always the last item evaluated in a topological sort
        return context[contextSize - 1]!!
    }

    /**
     * This method should implement the following structure to efficiently seek
     * and select concrete holders:
     * ```
     * return when (id.segment) {
     *   0 -> DependencyHolder0.create(buffer, id.position)
     *   1 -> DependencyHolder1.create(buffer, id.position)
     *   2 -> DependencyHolder2.create(buffer, id.position)
     *   else -> error("Invalid segment")
     * }
     * ```
     * */
    protected abstract fun instantiate(buffer: Array<Any?>, id: DependencyId): Any

    /**
     * Data Structure Format:
     * Index 0: Total instances needed (Context Size)
     * Index 1..N: Sequence of [ID, ArgCount, ArgIndex1, ArgIndex2...]
     * * Example: We want ServiceImpl (ID 2), which needs Repo (ID 1) and String (ID 0).
     * Array: [
     * 3,          // Context size
     * 0, 0,       // Create String (ID 0), requires 0 args. Stored at context index 0.
     * 1, 1, 0,    // Create Repo (ID 1), requires 1 arg from context index 0. Stored at context index 1.
     * 2, 2, 1, 0  // Create Service (ID 2), requires 2 args from context indices 1 and 0. Stored at context index 2.
     * ]
     */
    protected abstract fun getInstructions(id: DependencyId): IntArray?

}

