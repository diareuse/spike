package spike.factory

import spike.InternalSpikeApi

@InternalSpikeApi
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
public annotation class IncludedDeclarations(val names: Array<String>)
