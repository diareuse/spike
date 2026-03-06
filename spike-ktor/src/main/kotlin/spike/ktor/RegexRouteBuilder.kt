package spike.ktor

public interface RegexRouteBuilder : RouteBuilder {
    public val path: Regex
}
