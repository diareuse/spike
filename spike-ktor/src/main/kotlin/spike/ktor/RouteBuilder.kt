package spike.ktor

import io.ktor.server.routing.Route

public sealed interface RouteBuilder {
    public fun Route.build()
}
