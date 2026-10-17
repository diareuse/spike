package spike.jsr

import spike.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
public annotation class Named(val value: String)
