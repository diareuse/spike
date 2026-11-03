@spike.EntryPoint
interface App {
    val repo: Repo
    companion object
}

interface Repo

@spike.Include
@spike.test.Replaces(Repository::class)
class FakeRepo : Repo

@spike.Include
val FakeRepo.asRepo: Repo
    get() = this
