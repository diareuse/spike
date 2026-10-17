package bench.generator

import spike.Key

/**
 * Places a dialect in the order the benchmark lists frameworks in.
 */
@Key
@Retention(AnnotationRetention.SOURCE)
annotation class Order(val value: Int)
