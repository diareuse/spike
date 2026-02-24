package spike.ktor

interface RegexRouteBuilder : RouteBuilder {
    val path: Regex
}