package spike.factory

import kotlin.jvm.JvmInline

@JvmInline
public value class DependencyId(public val id: Int) {
    public val segment: Int get() = id shr 10
    public val position: Int get() = id and 0x3FF

    public constructor(segment: Int, position: Int) : this(
        segment.shl(10) + position.and(0x3FF)
    )
}