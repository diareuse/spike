package spike.compiler.graph

fun <T> onSmallStack(block: () -> T): T {
    var result: Result<T>? = null
    val thread = Thread(null, { result = runCatching(block) }, "small-stack", 128 * 1024)
    thread.isDaemon = true
    thread.start()
    thread.join(60_000)
    check(!thread.isAlive) { "block did not finish within 60 s" }
    return result!!.getOrThrow()
}
