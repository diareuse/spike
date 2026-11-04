package spike.compiler.processor

import spike.Include

@Include
class IndexNamesLiteralEscaped : IndexNamesLiteral {
    override fun of(names: String): String {
        val escaped = Escapes.fold(names) { text, (character, escape) -> text.replace(character, escape) }
        return "\"" + Control.replace(escaped) { "\\u%04x".format(it.value.single().code) } + "\""
    }

    private companion object {
        val Escapes = listOf(
            "\\" to "\\\\",
            "\"" to "\\\"",
            "$" to "\${'$'}",
            "\n" to "\\n",
            "\t" to "\\t",
            "·" to "\\u00b7",
            "♢" to "\\u2662",
        )
        val Control = Regex("[\\x00-\\x1f\\x7f-\\x9f]")
    }
}
