package spike.compiler.processor

import spike.Include

fun interface IndexNamesLiteral {
    fun of(names: String): String

    companion object {
        @Include
        val IndexNamesLiteralEscaped.literal: IndexNamesLiteral
            get() = this
    }
}
