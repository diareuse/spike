package spike.factory

import spike.InternalSpikeApi

@InternalSpikeApi
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
public annotation class ExportedUses(val entries: Array<String>)
