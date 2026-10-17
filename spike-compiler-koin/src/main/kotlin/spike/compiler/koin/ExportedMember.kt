package spike.compiler.koin

import com.squareup.kotlinpoet.TypeName

/**
 * A member of a Spike entry point that Koin can read as a factory of [type], through the code in [access].
 */
data class ExportedMember(val type: TypeName, val access: String)
