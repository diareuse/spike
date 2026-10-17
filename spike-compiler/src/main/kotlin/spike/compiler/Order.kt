package spike.compiler

import spike.Key

/**
 * Places a contribution to an ordered role: the composite applies its entries by ascending [value].
 */
@Key
@Retention(AnnotationRetention.SOURCE)
annotation class Order(val value: Int)
