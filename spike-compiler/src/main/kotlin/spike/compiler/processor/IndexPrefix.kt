package spike.compiler.processor

import spike.Key

/**
 * Keys an [IndexLookup] by the prefix of the index entries it resolves.
 */
@Key
@Retention(AnnotationRetention.SOURCE)
annotation class IndexPrefix(val value: String)
