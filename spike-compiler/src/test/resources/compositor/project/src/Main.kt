fun main() {
    checkNotNull(PropertyEntryPoint().obj)
    test.DependencyFactoryImpl().get<ComplexObject>(spike.factory.DependencyId(0))
    println("OK!")
}