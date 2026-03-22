package spike.factory

import kotlin.jvm.JvmInline

@JvmInline
public value class InstructionSetPointer(private val packed: Long) {
    public val offset: Int get() = (packed shr 32).toInt()
    public val size: Int get() = packed.toInt()
    public val endInclusive: Int get() = offset + size - 1

    public constructor(offset: Int, size: Int) : this(offset.toLong().shl(32) + size)
}