package spike.compiler.conversion

import spike.Key

/**
 * Places a contribution to an ordered role: the composite applies its entries by ascending [value].
 */
@Key
@Retention(AnnotationRetention.SOURCE)
annotation class Order(val value: Int)

/**
 * The entries of an ordered role, by ascending `@Order`.
 */
fun <T> Map<Int, T>.ordered(): List<T> = toSortedMap().values.toList()
