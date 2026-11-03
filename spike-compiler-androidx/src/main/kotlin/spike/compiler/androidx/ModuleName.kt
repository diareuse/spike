package spike.compiler.androidx

object ModuleName {
    fun encode(module: String): String = buildString {
        for (char in module) {
            when {
                char.isAsciiLetterOrDigit() -> append(char)
                char == '_' -> append("__")
                char == ':' -> append("_0")
                else -> append("_u").append(char.code.toString(16).padStart(4, '0'))
            }
        }
    }

    fun decode(name: String): String = decoded(name) ?: name

    private fun decoded(name: String): String? = buildString {
        var index = 0
        while (index < name.length) {
            val char = name[index]
            if (char != '_') {
                append(char)
                index += 1
                continue
            }
            when (name.getOrNull(index + 1)) {
                '_' -> append('_').also { index += 2 }
                '0' -> append(':').also { index += 2 }
                'u' -> {
                    val hex = name.substring(index + 2, minOf(index + 6, name.length))
                    if (hex.length != 4 || !hex.all(::isHexDigit)) return null
                    append(hex.toInt(16).toChar())
                    index += 6
                }
                else -> return null
            }
        }
    }

    private fun isHexDigit(char: Char) = char in '0'..'9' || char in 'a'..'f' || char in 'A'..'F'

    private fun Char.isAsciiLetterOrDigit() = this in 'a'..'z' || this in 'A'..'Z' || this in '0'..'9'
}
