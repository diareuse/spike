fun main() {
    PropertyEntryPoint().apply {
        val workers = workers
        workers["primary"]!!.doWork(1) {}
        workers["secondary"]!!.doWork(2) {}
    }
}

@spike.EntryPoint
interface PropertyEntryPoint {
    val workers: Map<String, IntWorker>

    companion object
}

@spike.Key
annotation class StringKey(val name: String)

interface Worker<Input, Message, Output> {
    fun doWork(input: Input, callback: (Message) -> Unit): Output
}

typealias IntWorker = Worker<Int, Int, Int?>

@spike.Include(bindTo = spike.BindTarget.Map, bindAs = IntWorker::class)
@StringKey("primary")
class Primary : IntWorker {
    override fun doWork(input: Int, callback: (Int) -> Unit): Int? {
        callback(input - 1)
        return if (input % 2 == 0) null else input
    }
}

@spike.Include(bindTo = spike.BindTarget.Map, bindAs = IntWorker::class)
@StringKey("secondary")
class Secondary : IntWorker {
    override fun doWork(input: Int, callback: (Int) -> Unit): Int? {
        callback(input + 1)
        return if (input % 2 == 0) null else input
    }
}
