import javax.inject.Inject

class Api @Inject constructor(val name: String) {
    @Inject
    constructor() : this("default")
}
