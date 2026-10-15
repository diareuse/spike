fun main() {
    val graph = NullableEntryPoint.factory().create(null)
    require(graph.token == null) { "direct member got ${graph.token}" }
    require(graph.tokenProvider.get() == null) { "provider got ${graph.tokenProvider.get()}" }
    require(graph.session.token == null) { "dependent got ${graph.session.token}" }
    require(NullableEntryPoint.factory().create("abc").tokenProvider.get() == "abc")
}

@spike.EntryPoint
interface NullableEntryPoint {
    val token: String?
    val tokenProvider: spike.Provider<String?>
    val session: Session

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(token: String?): NullableEntryPoint
    }

    companion object
}

@spike.Include
class Session(val token: String?)
