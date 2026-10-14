package spike.compiler.harness

import java.io.DataInputStream
import java.io.File

class BytecodeMethodSizes(private val classFile: File) {

    fun read(): Map<String, Int> = DataInputStream(classFile.inputStream().buffered()).use { input ->
        input.skipBytes(8)
        val utf8 = readConstantPool(input)
        input.skipBytes(6)
        input.skipBytes(2 * input.readUnsignedShort())
        repeat(input.readUnsignedShort()) { skipMember(input) }
        val sizes = mutableMapOf<String, Int>()
        repeat(input.readUnsignedShort()) {
            input.skipBytes(2)
            val name = utf8.getValue(input.readUnsignedShort())
            input.skipBytes(2)
            repeat(input.readUnsignedShort()) {
                val attribute = utf8.getValue(input.readUnsignedShort())
                val length = input.readInt()
                if (attribute == "Code") {
                    input.skipBytes(4)
                    sizes[name] = maxOf(sizes[name] ?: 0, input.readInt())
                    input.skipBytes(length - 8)
                } else {
                    input.skipBytes(length)
                }
            }
        }
        sizes
    }

    private fun readConstantPool(input: DataInputStream): Map<Int, String> {
        val utf8 = mutableMapOf<Int, String>()
        val count = input.readUnsignedShort()
        var index = 1
        while (index < count) {
            when (val tag = input.readUnsignedByte()) {
                1 -> utf8[index] = input.readUTF()
                3, 4, 9, 10, 11, 12, 17, 18 -> input.skipBytes(4)
                5, 6 -> input.skipBytes(8).also { index++ }
                7, 8, 16, 19, 20 -> input.skipBytes(2)
                15 -> input.skipBytes(3)
                else -> error("Unknown constant pool tag $tag in $classFile")
            }
            index++
        }
        return utf8
    }

    private fun skipMember(input: DataInputStream) {
        input.skipBytes(6)
        repeat(input.readUnsignedShort()) {
            input.skipBytes(2)
            input.skipBytes(input.readInt())
        }
    }

}
