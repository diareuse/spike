package spike

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
public annotation class EntryPoint {
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.BINARY)
    public annotation class Factory {
        @InternalSpikeApi
        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.BINARY)
        public annotation class SharesSingletons
    }
}
