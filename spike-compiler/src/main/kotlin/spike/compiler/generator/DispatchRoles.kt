package spike.compiler.generator

import spike.Include
import spike.Qualifier

/**
 * Dispatch for dependencies built without runtime arguments, through `get`.
 */
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class PlainBranches

/**
 * Dispatch for assisted dependencies built from runtime arguments, through `assisted`.
 */
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class AssistedBranches

private const val GET_CHUNK_SIZE = 256

@Include
@PlainBranches
fun plainBranches(core: DispatchGet): DispatchFunctions = DispatchGetChunked(GET_CHUNK_SIZE, core)

@Include
@AssistedBranches
val DispatchAssisted.asDispatch: DispatchFunctions
    get() = this
