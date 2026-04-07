import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import spike.factory.DependencyFactory
import spike.factory.DependencyId
import spike.factory.InstructionSetPointer

public object PropertyEntryPoint_Factory : DependencyFactory() {
  override val maxConstructorArgs: Int = 2

  override val instructionSet: IntArray
    get() = PropertyEntryPoint_InstructionSet.memory

  override fun getInstructionsPointer(id: DependencyId): InstructionSetPointer? = when (id.id) {
    0 -> InstructionSetPointer(0, 9)
    2 -> InstructionSetPointer(9, 3)
    1 -> InstructionSetPointer(12, 3)
    3 -> InstructionSetPointer(15, 9)
    5 -> InstructionSetPointer(24, 3)
    4 -> InstructionSetPointer(27, 3)
    6 -> InstructionSetPointer(30, 9)
    8 -> InstructionSetPointer(39, 3)
    7 -> InstructionSetPointer(42, 3)
    else -> error("Invalid identifier ${id}")
  }

  override fun instantiate(buffer: Array<Any?>, id: DependencyId): Any = when (id.segment) {
      0 -> PropertyEntryPoint_DependencyHolder0.create(buffer, id.position)
      else -> error("Invalid segment")
  }
}
