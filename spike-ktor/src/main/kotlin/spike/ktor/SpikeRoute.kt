package spike.ktor

import io.ktor.server.routing.Routing
import io.ktor.server.routing.route

fun Routing.spike(entryPoint: BaseRouteEntryPoint) {
    for (route in entryPoint.routes) when (route) {
        is RegexRouteBuilder -> route(route.path) {
            with(route) { build() }
        }

        is RegularRouteBuilder -> route(route.path) {
            with(route) { build() }
        }
    }
}