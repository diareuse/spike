package spike.ktor

import io.ktor.server.routing.Route

sealed interface RouteBuilder {
    fun Route.build()
}