package spike.compiler.conversion

/**
 * A subject converted into one @Include function, written to its own file named by [packageName] and [fileName].
 */
interface Included : Declared {
    val packageName: String
    val fileName: String
}
