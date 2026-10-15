package spike.compiler.graph

internal fun Type.importName(): String {
    val text = toString()
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
