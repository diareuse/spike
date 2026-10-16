package spike.compiler.graph

internal fun Type.importName(): String = toString().escapedName()

internal fun String.escapedName(): String {
    val text = this
    return buildString {
        for ((index, char) in text.withIndex()) {
            val next = text.getOrNull(index + 1)
            when {
                char.isLetterOrDigit() -> append(char)
                char == '.' && next != null && next.isLetter() -> append('_')
                else -> append('_').append(escapes[char] ?: char.escapedCode())
            }
        }
    }
}

internal fun String.unescapedName(): String = buildString {
    var index = 0
    val text = this@unescapedName
    while (index < text.length) {
        val char = text[index++]
        if (char != '_') {
            append(char)
            continue
        }
        val next = text[index++]
        when {
            next.isLetter() -> append('.').append(next)
            next == '9' -> append(text.substring(index, index + CODE_LENGTH).toInt(HEX).toChar()).also {
                index += CODE_LENGTH
            }
            else -> append(unescapes.getValue(next))
        }
    }
}

private val unescapes by lazy { escapes.entries.associate { (char, code) -> code.single() to char } }

private fun Char.escapedCode() = "9" + code.toString(HEX).padStart(CODE_LENGTH, '0')

private const val HEX = 16
private const val CODE_LENGTH = 4

private val escapes = mapOf(
    '_' to "0",
    '?' to "1",
    '<' to "2",
    '>' to "3",
    ',' to "4",
    ' ' to "5",
    '@' to "6",
    '(' to "7",
    ')' to "8",
)
